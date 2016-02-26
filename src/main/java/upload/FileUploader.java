package upload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class FileUploader {

    private static String FILE_DIR;

    public FileUploader(String FILE_DIR) {
        this.FILE_DIR = FILE_DIR;
    }

    public void initUpload(HttpServletRequest req, HttpServletResponse res){

    }


    public void saveData(){
        try(Writer writer = new FileWriter(FILE_DIR + "/destroyable")) {
            writer.write("File destroyer has been destroyed at: " + new Date().toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
