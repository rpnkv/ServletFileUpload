package upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "upload.UploadServlet", urlPatterns = {"/upload/*"})
public class UploadServlet extends HttpServlet {

    FileUploader uploader = new FileUploader(getServletContext().getInitParameter("filePath"));

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        switch (pathInfo){
            case "init":

                break;
            case "chunk":

                break;
            default:
                System.out.println("Unknown URL");
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("Second servlet says hello, but it can't process 'GET' requests.");
        /*JSONObject json      = new JSONObject();
        JSONArray addresses = new JSONArray();
        JSONObject address;
        try
        {
            int count = 15;

            for (int i=0 ; i<count ; i++)
            {
                address = new JSONObject();
                address.put("CustomerName"     , "Decepticons" + i);
                address.put("AccountId"        , "1999" + i);
                address.put("SiteId"           , "1888" + i);
                address.put("Number"            , "7" + i);
                address.put("Building"          , "StarScream Skyscraper" + i);
                address.put("Street"            , "Devestator Avenue" + i);
                address.put("City"              , "Megatron City" + i);
                address.put("ZipCode"          , "ZZ00 XX1" + i);
                address.put("Country"           , "CyberTron" + i);
                addresses.put(address);
            }
            json.put("Addresses", addresses);
        }
        catch (JSONException jse)
        {

        }
        response.setContentType("application/json");
        response.getWriter().write(json.toString());*/
    }

    @Override
    public void destroy() {
        System.out.println("DESTROY");
        uploader.saveData();
        super.destroy();
    }
}
