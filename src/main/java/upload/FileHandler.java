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

    private UploadsCollection uploads;
    private UploadPerformer uploadPerformer;

    public FileHandler(String FILE_DIR) {
        uploads = new UploadsCollection(FILE_DIR);
        uploadPerformer = new UploadPerformer();
    }


    public void tryInitUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            initUpload(request,response);
        } catch (InvalidParameterException ex) {
            response.setStatus(500);
        }
    }

    private void initUpload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UploadData newUploadData = UploadData.createFileIdByRequest(request);

        insertEntryInCollection(newUploadData);

        requestNextDataChunk(newUploadData, response);
    }

    private void insertEntryInCollection(UploadData uploadData) throws IOException {
        if (!uploads.contains(uploadData)) {
            uploads.addNew(uploadData);
        }else{
            UploadData existingUploadData = uploads.getContainingEqual(uploadData);
            uploadPerformer.resetUploadDataIfFileWasUploaded(existingUploadData);
        }
    }

    public void processIncomingDataChunk(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Part filePart = request.getPart("file");
        UploadData uploadData = UploadData.createFileIdByRequest(request);

        uploadData = uploads.getContainingEqual(uploadData);

        if (uploadData == null) {
            response.sendError(413, "Uploading chunk cannot be associated with any file.");
            return;
        }

        if(uploadPerformer.trySaveDataChunk(filePart,uploadData)){
            sendUploadSuccessInfo(response);
        }else{
            requestNextDataChunk(uploadData,response);
        }

    }

    private void requestNextDataChunk(UploadData uploadData, HttpServletResponse response) {
        try {
            uploadPerformer.appendNextDataChunkRequestToResponse(uploadData, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendUploadSuccessInfo(HttpServletResponse response) throws IOException {

        uploadPerformer.appendSuccessUploadMessageToResponse(response);

    }
}
