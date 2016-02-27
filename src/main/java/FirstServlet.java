import upload.FileUploader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

@WebServlet(name="firstServlet", urlPatterns = {"/timeaction"})
@MultipartConfig
public class FirstServlet extends HttpServlet {

    private ServletContext context;

    private String path;

    @Override
    public void init() throws ServletException {
        super.init();
        context = getServletContext();

        path = context.getInitParameter("filePath");


    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = filePart.getSubmittedFileName();
        System.out.println(fileName);
        InputStream fileContent = filePart.getInputStream();
        System.out.println("file lenght, bytes: " + fileContent.available());

        FileOutputStream fos = new FileOutputStream(path + "/" + fileName);


        byte[] buffer = new byte[fileContent.available()];

        fileContent.read(buffer);
        fileContent.close();

        fos.write(buffer);

        fos.close();

        request.setAttribute("attr","LELELELE");

    }

}
