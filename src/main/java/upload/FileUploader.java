package upload;

import models.FileData;
import models.FileId;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
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
        try {
            FileId newFileUploadId = FileId.createFileIdByRequest(reqest);
            if (!filesUploadsInfo.containsKey(newFileUploadId)) {
                initNewFileUpload(newFileUploadId, response);
            }

            continueFileUpload(newFileUploadId, response);

        } catch (InvalidParameterException ex) {
            response.setStatus(500);
        }
    }

    private void initNewFileUpload(FileId newFileId, HttpServletResponse response) {
        String filePath = createPathForFile(newFileId);

        filesUploadsInfo.put(newFileId, new FileData(filePath, 0));
        try {
            Files.createFile(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void continueFileUpload(FileId fileId, HttpServletResponse response) {
        String jsonError = null;

        FileData uploadData = filesUploadsInfo.get(fileId);

        long nextChunkStartIndex = uploadData.getChunksDownloaded() * FileData.chunkSize,
                nextChunkEndIndex;
        if (nextChunkStartIndex * FileData.chunkSize < fileId.getSize()) {
            nextChunkEndIndex = nextChunkStartIndex + FileData.chunkSize;
        } else {
            nextChunkEndIndex = nextChunkStartIndex + (fileId.getSize() - nextChunkStartIndex + FileData.chunkSize);
        }


        JSONObject json = new JSONObject();
        try {
            json.put("nextChunkStartIndex", nextChunkStartIndex);
            json.put("nextChunkEndIndex", nextChunkEndIndex);
        } catch (JSONException jse) {
            jsonError = "Cannot create json for file " + fileId;
        }

        try {
            if (jsonError == null) {
                response.setContentType("application/json");
                response.getWriter().write(json.toString());
            }else{
                response.sendError(500,jsonError);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = filePart.getSubmittedFileName();
        System.out.println(fileName);
        InputStream fileContent = filePart.getInputStream();
        System.out.println("file lenght, bytes: " + fileContent.available());

        BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent));

        System.out.println(reader.readLine());

    }

    private String createPathForFile(FileId id) {
        return FILE_DIR + "/" + id.getUsername() + "-" + id.getFileName() + "-" + new Date().getTime();
    }
}
