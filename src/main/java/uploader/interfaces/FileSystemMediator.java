package uploader.interfaces;

import uploader.models.UploadFileSystemProperties;

import javax.servlet.http.Part;
import java.io.IOError;

public interface FileSystemMediator {

    void appendChunk(Part chunk, UploadFileSystemProperties properties);
    void deleteFile(UploadFileSystemProperties properties);
    void cleanFile(UploadFileSystemProperties properties);

}

