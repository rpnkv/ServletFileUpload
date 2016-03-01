package uploader.interfaces;

import uploader.models.UploadFileSystemProperties;
import uploader.models.UploadId;

public interface UploadPropertiesKeeper {

    boolean addNew(UploadId uploadId);
    UploadId getEqualId(UploadId uploadId);
    UploadFileSystemProperties getPropertiesForId(UploadId uploadId);
    void deleteRecord(UploadId uploadId);
    boolean isUploadComplete(UploadId uploadId);

}
