package uploader.interfaces;

public interface Responder {

    void requestNextChunk();
    void informAboutSuccessfulUpload();
    void informAboutSuccessfulCancel();
    void informAboutError(Exception e);
}
