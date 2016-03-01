package uploader;

import uploader.exceptions.InvalidParameterException;
import uploader.interfaces.FileSystemMediator;
import uploader.interfaces.Responder;
import uploader.interfaces.UploadPropertiesKeeper;
import uploader.models.UploadId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadController {

    FileSystemMediator fileSystemMediator;
    Responder responder;
    UploadPropertiesKeeper uploadPropertiesKeeper;

    public UploadController(FileSystemMediator fileSystemMediator, Responder responder, UploadPropertiesKeeper uploadPropertiesKeeper) {
        this.fileSystemMediator = fileSystemMediator;
        this.responder = responder;
        this.uploadPropertiesKeeper = uploadPropertiesKeeper;
    }

    public void initUpload(HttpServletRequest request, HttpServletResponse response) {
        try {
            UploadId uploadId = UploadId.createFileIdByRequest(request);
            addNewIdAndDeletePreviousIfRequired(uploadId);


        } catch (InvalidParameterException e) {
            responder.informAboutError(e);
        }
    }

    private void addNewIdAndDeletePreviousIfRequired(UploadId uploadId) {


    }

    public void receiveChunk(HttpServletRequest request, HttpServletResponse response) {

    }


    public void cancelUpload(HttpServletRequest request, HttpServletResponse response) {

    }
}
