package com.testing;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class accepts image data from android application,
 * decodes it and stores the image in hard-drive
 * NOTE : It has only POST method because we cannot send large data
 *        in GET method as a part of URL because there is a limit for
 *        URL length
 */

@WebServlet(urlPatterns = "/androidPattern",name = "androidName")
public class TestAndroidFileUpload extends HttpServlet
{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
        //Extract the data from HTTP message body (POST Method used)
        String data = req.getParameter("key");
        
        //The message is in Base64 encoded string form (used to transfer binary data reliably)
        byte[] bytes = Base64.getDecoder().decode(data);
        
        //Create OutputStream and write the data
        OutputStream os = new FileOutputStream("C:\\Users\\a\\Desktop\\New folder\\done.jpg");
        os.write(bytes);
        os.flush();
        os.close();
        
        //Send some response to the application
        resp.getWriter().print("Done boss");        
    }
}
