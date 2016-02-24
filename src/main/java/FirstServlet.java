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

    @Override
    public void init() throws ServletException {
        super.init();
        context = getServletContext();

        context.setAttribute("initTime",new Date());

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
        GregorianCalendar gc = new GregorianCalendar();
        String timeJsp = request.getParameter("time");

        String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = filePart.getSubmittedFileName();
        System.out.println(fileName);
        InputStream fileContent = filePart.getInputStream();
        System.out.println("file lenght, bytes: " + fileContent.available());

        BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent));

        String line;

        System.out.println("File's content: ");
        while ((line = reader.readLine()) != null){
            System.out.println(line);
        }

        float delta = ((float)(gc.getTimeInMillis() - Long.parseLong(timeJsp)))/1000;
        request.setAttribute("res", delta);
        request.setAttribute("title","Result page");
        request.setAttribute("color","green");
        request.setAttribute("email",this.getServletContext().getInitParameter("administrator"));
        request.setAttribute("initTime",context.getAttribute("initTime"));
        request.setAttribute("contextParams",getContextParamNames());
        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }

    private String[] getContextParamNames(){
        List<String> paramsList = new LinkedList<>();

        Enumeration<String> paramsEnum = context.getAttributeNames();

        while (paramsEnum.hasMoreElements()){
            String attribute = paramsEnum.nextElement();
            paramsList.add(attribute);
        }

        return paramsList.toArray(new String[paramsList.size()]);
    }
}
