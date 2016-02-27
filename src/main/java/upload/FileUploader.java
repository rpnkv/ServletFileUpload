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
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.*;

public class FileUploader {

    private static String FILE_DIR;
    private List<FileId> filesUploadsInfo;


    public FileUploader(String FILE_DIR) {
        FileUploader.FILE_DIR = FILE_DIR;
        filesUploadsInfo = new ArrayList<>();
    }


    public void initUpload(HttpServletRequest reqest, HttpServletResponse response) {
        try {
            FileId newFileUploadId = FileId.createFileIdByRequest(reqest);
            if (!filesUploadsInfo.contains(newFileUploadId)) {
                initNewFileUpload(newFileUploadId, response);
            }

            continueFileUpload(newFileUploadId, response);

        } catch (InvalidParameterException ex) {
            response.setStatus(500);
        }
    }

    private void initNewFileUpload(FileId newFileId, HttpServletResponse response) {
        String filePath = createPathForFile(newFileId);

        newFileId.setFileData(new FileData(filePath, 0));

        filesUploadsInfo.add(newFileId);
        try {
            Files.createFile(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void continueFileUpload(FileId fileId, HttpServletResponse response) {
        String jsonError = null;

        FileId uploadData = filesUploadsInfo.get(filesUploadsInfo.indexOf(fileId));

        long nextChunkStartIndex = uploadData.getFileData().getChunksDownloaded() * FileData.chunkSize,
                fileSize = fileId.getSize(),
                nextChunkEndIndex;

        if (nextChunkStartIndex + FileData.chunkSize < fileSize) {
            nextChunkEndIndex = nextChunkStartIndex + FileData.chunkSize;
        } else {
            nextChunkEndIndex = nextChunkStartIndex + (fileSize - nextChunkStartIndex + FileData.chunkSize);
        }


        JSONObject json = new JSONObject();
        try {
            json.put("uploadStatus","uploading");
            json.put("nextChunkStartIndex", nextChunkStartIndex);
            json.put("nextChunkEndIndex", nextChunkEndIndex);
        } catch (JSONException jse) {
            jsonError = "Cannot create json for file " + fileId;
        }

        try {
            if (jsonError == null) {
                response.setContentType("application/json");
                response.getWriter().write(json.toString());
                System.out.println("requesting chunks from " + nextChunkStartIndex + " " + nextChunkEndIndex + "...");
            }else{
                response.sendError(500,jsonError);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        FileId fileId = FileId.createFileIdByRequest(request);

        fileId = filesUploadsInfo.get(filesUploadsInfo.indexOf(fileId));

        processIncomingData(filePart,fileId);

        if(!fileId.getFileData().haveWeGotLastChunk(fileId.getSize())){
            continueFileUpload(fileId,response);
        }else{
            System.out.println("we've got full file.");
        }


    }

    private void processIncomingData(Part filePart, FileId fileId) {
        showStreamContent(filePart);

        fileId.getFileData().incChunksCounter();
    }

    private void showStreamContent(Part fileContent){
        String newLine;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(fileContent.getInputStream()))){
            while ((newLine = reader.readLine()) != null){
                System.out.println(newLine);
            }
        } catch (IOException e) {
            System.out.println(e);
        }


    }

    private String createPathForFile(FileId id) {
        return FILE_DIR + "/" + id.getUsername() + "-" + id.getFileName() + "-" + new Date().getTime();
    }
}
