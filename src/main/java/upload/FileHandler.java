package upload;

import Services.UploadPerformer;
import Services.UploadsCollection;
import models.UploadData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.security.InvalidParameterException;

public class FileHandler {

    private static String FILE_DIR;
    private UploadsCollection uploadInfos;
    private UploadPerformer uploadPerformer;

    public FileHandler(String FILE_DIR) {
        FileHandler.FILE_DIR = FILE_DIR;
        uploadInfos = new UploadsCollection();
        uploadPerformer = new UploadPerformer();
    }


    public void initUpload(HttpServletRequest request, HttpServletResponse response) {
        try {
            UploadData newUploadData = UploadData.createFileIdByRequest(request);

            if (!uploadInfos.contains(newUploadData)) {
                uploadInfos.addNew(newUploadData);
            }

            UploadData uploadData = uploadInfos.getContainingEqual(newUploadData);

            requestNextDataChunk(uploadData, response);

        } catch (InvalidParameterException ex) {
            response.setStatus(500);
        }
    }

    public void processIncomingDataChunk(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        UploadData uploadData = UploadData.createFileIdByRequest(request);

        uploadData = uploadInfos.getContainingEqual(uploadData);

        if (uploadData == null) {
            response.sendError(413, "Uploading chunk cannot be associated with any file.");
            return;
        }

        switch (uploadPerformer.saveDataChunk(filePart, uploadData)) {
            case chunkSaved:
                requestNextDataChunk(uploadData, response);
                break;
            case descriptionError:
                response.sendError(500, "Upload data is corrupted. Cannot continue file upload.");
                break;
            case ioError:
                response.sendError(500, "Cannot perform file operations now.");
                break;
            case fileUploaded:
                uploadInfos.delete(uploadData);
                sendUploadSuccessInfo(response);
                break;
        }
    }

    private void requestNextDataChunk(UploadData uploadData, HttpServletResponse response) {
        try {
            uploadPerformer.appendNextDataChunkRequestToResponse(uploadData, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendUploadSuccessInfo(HttpServletResponse response) {

        uploadPerformer.appendSuccessUploadMessageToResponse(response);

    }

    public static String getFileDir() {
        return FILE_DIR;
    }
}
