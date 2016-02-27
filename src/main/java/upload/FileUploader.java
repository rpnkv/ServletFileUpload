package upload;

import com.sun.javaws.exceptions.InvalidArgumentException;
import models.FileData;
import models.FileId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileUploader {

     private static String FILE_DIR;
    private Map<FileId, FileData> filesUploadsInfo;


    public FileUploader(String FILE_DIR) {
       FileUploader.FILE_DIR = FILE_DIR;
       filesUploadsInfo = new HashMap<>();
    }


    public void initUpload(HttpServletRequest reqest, HttpServletResponse response) {
       try{
            FileId newFileUploadId = FileId.createFileIdByRequest(reqest);
            if(!filesUploadsInfo.containsKey(newFileUploadId)){
                initNewFileUpload(newFileUploadId,response);
            }else{
                continueFileUpload(newFileUploadId,response);
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    private void initNewFileUpload(FileId newFileId, HttpServletResponse response){
        int q = 2;
    }

    private void continueFileUpload(FileId newFileId, HttpServletResponse response){
        int w = 4;
    }

    public void saveData() {

    }
}
