package com.testing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet(urlPatterns = "/testPattern",name = "testName")
public class TestServlet1 extends HttpServlet
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        Part part = req.getPart("fileName");
		
        InputStream is = part.getInputStream();
        byte[] data = new byte[is.available()];
        is.read(data);
        
        OutputStream os = new FileOutputStream("C:\\Users\\a\\Documents\\MehulDocuments\\SEM 6\\imageTest.jpg");
        os.write(data);
       
        is.close();
        os.flush();
        os.close();
        
        resp.getWriter().print("<h1>" + part.getSubmittedFileName() + "</h1>");
        resp.getWriter().print("<h1>" + part.getContentType() + "</h1>");
    }
    
}
