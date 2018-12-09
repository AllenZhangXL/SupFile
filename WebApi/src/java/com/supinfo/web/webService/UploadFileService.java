package com.supinfo.web.webService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supinfo.web.dao.ItemDao;
import com.supinfo.web.dao.UserDao;
import com.supinfo.web.entity.Customer;
import com.supinfo.web.entity.item;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/file")
public class UploadFileService {

    @EJB
    UserDao userDao;

    @EJB
    ItemDao itemDao;

    @PersistenceContext
    EntityManager em;

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("id") Long ownerId,
            @FormDataParam("superId") Long superId,
            @FormDataParam("isFolder") boolean isFolder
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String uploadedFileLocation = "c://uploaded/"
                + fileDetail.getFileName();

        File file = new File(uploadedFileLocation);
        Customer c = userDao.getCustomerById(ownerId);
        if (c.getAvailableSpace() >= file.length()) {
            writeToFile(uploadedInputStream, uploadedFileLocation);
            Long space = c.getAvailableSpace();
            space -= file.length();
            c.setAvailableSpace(space);

            double db = Double.valueOf(space) / 1073741824;
            BigDecimal b = new BigDecimal(db);
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            userDao.updateUser(c);
            itemDao.addNewItem(creatNewItem(ownerId, superId, fileDetail.getFileName(), isFolder, file.length(), uploadedFileLocation));
            List<item> items = itemDao.getAllfilesById(superId, ownerId);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 200);
            map.put("availableSpace", f1);
            map.put("items", items);
            String json = mapper.writeValueAsString(map);
            return json;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status", 400);
            map.put("msg", "too big to upload");
            String json = mapper.writeValueAsString(map);
            return json;
        }
    }

    private void writeToFile(InputStream uploadedInputStream,
            String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("/get_items")
    @Produces(MediaType.APPLICATION_JSON)
    public List<item> getItemsById(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
        Long superId = Long.valueOf(map.get("superId").toString());
        Long ownerId = Long.valueOf(map.get("ownerId").toString());
        List<item> items = itemDao.getAllfilesById(superId, ownerId);
        return items;
    }

    private item creatNewItem(Long ownerId, Long superId, String name, boolean type, Long fileSize, String path) {
        item i = new item();
        i.setFileSize(fileSize);
        i.setName(name);
        i.setSuperId(superId);
        i.setIsFolder(type);
        i.setOwnerId(ownerId);
        i.setPath(path);
        return i;
    }

    @POST
    @Path("/rename")
    public String rename(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(
                json,
                new TypeReference<Map<String, Object>>() {
                });
        String newFileName = map.get("newFileName").toString();
        Long itemId = Long.parseLong(map.get("itemId").toString());
        Long ownerId = Long.parseLong(map.get("ownerId").toString());
        Long superId = Long.parseLong(map.get("superId").toString());
        item i = itemDao.getItemById(itemId);
        File oldfile = new File("c://uploaded/" + i.getName());
        File newfile = new File("c://uploaded/" + newFileName);
        if (oldfile.renameTo(newfile)) {
            i.setName(newFileName);
            em.merge(i);
            Map<String,Object> maps = new HashMap<String,Object>();
            maps.put("status", 200);
            maps.put("items", itemDao.getAllfilesById(superId, ownerId));
            String resultJson = mapper.writeValueAsString(maps);
            return resultJson;
        } else {
            String resultJson = "{'status' : '400'}";
            return resultJson;
        }
    }

    @GET
    @Path("/download/{id}")
    @Produces("application/zip")
    public Response download(@PathParam("id") Long id) throws FileNotFoundException, IOException {
        item i = itemDao.getItemById(id);
        String filePath = i.getPath();
        String uuid = UUID.randomUUID().toString();
        compress(filePath, "C:\\uploaded/" + uuid.concat(".zip"));

        String fileName = uuid.concat(".zip");
        String path = "C:\\uploaded/" + uuid.concat(".zip");
        File file = new File(path);
        ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition",
                "attachment; filename=\"" + fileName + "\"");
        return response.build();
    }

    private static void compress(String srcFilePath, String destFilePath) {

        File src = new File(srcFilePath);
        File zipFile = new File(destFilePath);

        try {

            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            String baseDir = "";

            try {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
                ZipEntry entry = new ZipEntry(baseDir + src.getName());
                zos.putNextEntry(entry);
                int count;
                byte[] buf = new byte[1024];
                while ((count = bis.read(buf)) != -1) {
                    zos.write(buf, 0, count);
                }
                bis.close();

            } catch (Exception e) {
            }
            zos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @POST
    @Path("/folder")
    public String createFolder(String folderJson) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map = mapper.readValue(folderJson, new TypeReference<Map<String, Object>>() {});
        String folderName = map.get("folderName").toString();
        Long superId = Long.valueOf(map.get("superId").toString());
        Long ownerId = Long.valueOf(map.get("ownerId").toString());
//        String folderName = "test folder";
//        Long ownerId = Long.parseLong("1");
//        Long superId = Long.parseLong("1");
        item i = new item();
        i.setIsFolder(true);
        i.setName(folderName);
        i.setOwnerId(ownerId);
        i.setSuperId(superId);
        itemDao.addNewItem(i);
        Map<String,Object> maps = new HashMap<String,Object>();
        maps.put("status", 200);
        maps.put("items", itemDao.getAllfilesById(superId, ownerId));
        String json = mapper.writeValueAsString(maps);
        return json;
    }
}