import upload.FileHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UploadServlet", urlPatterns = {"/upload/*"})
@MultipartConfig
public class UploadServlet extends HttpServlet {

    private ServletContext context;
    private FileHandler uploader;

    @Override
    public void init() throws ServletException {
        super.init();
        context = getServletContext();
        uploader = new FileHandler(context.getInitParameter("filePath"));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        switch (pathInfo){
            case "/init":
                uploader.tryInitUpload(request, response);
                break;
            case "/chunk":
                uploader.processIncomingDataChunk(request, response);
                break;
            default:
                System.out.println("Unknown URL");
                break;
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("Second servlet says hello, but it can't process 'GET' requests.");
    }
}
