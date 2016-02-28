package upload;

import Services.JSONOperator;
import models.FileData;
import models.FileId;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
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

        FileId uploadData = filesUploadsInfo.get(filesUploadsInfo.indexOf(fileId));

        long nextChunkStartIndex = uploadData.getFileData().getChunksDownloaded() * FileData.chunkSize,
                fileSize = fileId.getSize(),
                nextChunkEndIndex;

        if (nextChunkStartIndex + FileData.chunkSize < fileSize) {
            nextChunkEndIndex = nextChunkStartIndex + FileData.chunkSize;
        } else {
            nextChunkEndIndex = nextChunkStartIndex + (fileSize - nextChunkStartIndex);
        }

        JSONOperator.appendMapAsJSONToResponse(response, uploadDataToMap(nextChunkStartIndex,nextChunkEndIndex,"uploading"));
    }

    private Map<String,String> uploadDataToMap(long nextChunkStartIndex, long nextChunkEndIndex, String uploadStatus){
        Map<String,String> values = new HashMap<>(6);
        values.put("uploadStatus", uploadStatus);
        values.put("nextChunkStartIndex", String.valueOf(nextChunkStartIndex));
        values.put("nextChunkEndIndex", String.valueOf(nextChunkEndIndex));

        return values;
    }

    public void saveData(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        FileId fileId = FileId.createFileIdByRequest(request);

        fileId = filesUploadsInfo.get(filesUploadsInfo.indexOf(fileId));

        processIncomingData(filePart, fileId);

        if (!fileId.getFileData().haveWeGotLastChunk(fileId.getSize())) {
            continueFileUpload(fileId, response);
        } else {
            endFileUpload(fileId, response);
        }

    }

    private void endFileUpload(FileId fileId, HttpServletResponse response) {
        String jsonError=  null;

        JSONObject json = new JSONObject();
        try {
            json.put("uploadStatus", "uploaded");
        } catch (JSONException jse) {
            jsonError = "Cannot create json for file " + fileId;
        }

        try {
            if (jsonError == null) {
                response.setContentType("application/json");
                response.getWriter().write(json.toString());
                System.out.println("We've got all file");
            } else {
                response.sendError(500, jsonError);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processIncomingData(Part filePart, FileId fileId) {

        try {
            if(appendDataToFile(filePart.getInputStream(), fileId.getFileData().getPath())){
                fileId.getFileData().incChunksCounter();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private boolean appendDataToFile(InputStream inputStream, String fileName) throws IOException{

        try(FileOutputStream fos = new FileOutputStream(fileName,true)) {
            byte[] incomingBytes = new byte[inputStream.available()];

            inputStream.read(incomingBytes);

            fos.write(incomingBytes);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String createPathForFile(FileId id) {
        return FILE_DIR + "/" + id.getUsername() + "-" + id.getFileName() + "-" + new Date().getTime();
    }
}
