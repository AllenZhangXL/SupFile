<%-- 
    Document   : FileUpload
    Created on : Jun 4, 2018, 12:03:42 AM
    Author     : guoyo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="rest/file/upload" method="post" enctype="multipart/form-data">

            <p>
                Select a file : <input type="file" name="file" size="45" />
            </p>
            <input type="hidden"  name="id"  value="1" />
            <input type="hidden"  name="superId"  value="1" />
            <input type="hidden" name="isFolder" value="false">
                   <input type="submit" value="Upload It" />

        </form>
    </body>
</html>
