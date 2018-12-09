/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.web.webService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supinfo.web.dao.UserDao;
import com.supinfo.web.entity.Customer;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/user")
public class UserWebService {

    @EJB
    UserDao userDao;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserById(@PathParam("id") Long id) throws JsonProcessingException {
        //System.out.println("getUserById" + userDao.getCustomerById(id).getEmail());
        ObjectMapper mapper = new ObjectMapper();
//        Map<String,Object> maps = new HashMap<String, Object>();
//        maps.put("status", "400");
//        maps.put("entity", userDao.getCustomerById(id));
//        String json = mapper.writeValueAsString(maps);
//        return Response.ok(json).build();
        Customer uLogin = userDao.getCustomerById(id);
        double db = Double.valueOf(uLogin.getAvailableSpace()) / 1073741824;
        BigDecimal b = new BigDecimal(db);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("status", "200");
        maps.put("id", uLogin.getId());
        maps.put("email", uLogin.getEmail());
        maps.put("space", f1);
        String json = mapper.writeValueAsString(maps);
        return json;
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String register(String userJson) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Customer user = mapper.readValue(userJson, Customer.class);
        Customer result = userDao.register(user);
        if (result == null) {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("status", "400");
            maps.put("msg", "incorrect username");
            String json = mapper.writeValueAsString(maps);
            return json;
        } else {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("status", "200");
            maps.put("entity", result);
            String json = mapper.writeValueAsString(maps);
            return json;
        }

    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String Login(String user) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(user, new TypeReference<Map<String, Object>>() {
        });
        String email = map.get("username").toString();
        String password = map.get("password").toString();
        Customer uLogin = userDao.login(email, password);

        if (uLogin == null) {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("status", "400");
            maps.put("msg", "incorrect username");
            String json = mapper.writeValueAsString(maps);
            return json;
        } else {
            double db = Double.valueOf(uLogin.getAvailableSpace()) / 1073741824;
            BigDecimal b = new BigDecimal(db);
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //String jsonString = mapper.writeValueAsString(uLogin);
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("status", "200");
            maps.put("id", uLogin.getId());
            maps.put("email", uLogin.getEmail());
            maps.put("availableSpace", f1);
            String json = mapper.writeValueAsString(maps);
            return json;
        }
    }
}
