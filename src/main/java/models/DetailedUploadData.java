package models;

import java.nio.file.Path;

public class DetailedUploadData extends UploadData{

    private Path path;
    private long chunksDownloaded;

    private int chunkSize = 1024 * 1024 * 8;

    public DetailedUploadData(UploadData uploadData, Path path, long chunksDownloaded) {
        super(uploadData.username, uploadData.fileName, uploadData.size, uploadData.modifyDate);
        this.path = path;
        this.chunksDownloaded = chunksDownloaded;
    }

    public Path getPath() {
        return path;
    }

    public long getChunksDownloaded() {
        return chunksDownloaded;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void incDownloadedChunksCounter(){
        chunksDownloaded++;
    }

    public void resetChunksCounter(){
        chunksDownloaded = 0;
    }

    public boolean allChunksAreUploaded(){
       return size <= chunksDownloaded * chunkSize;
    }
}
