package models;

public class FileData {

    public static final int chunkSize = 32;
    private String path;
    private long chunksDownloaded;

    public FileData(String path, long chunksDownloaded) {
        this.path = path;
        this.chunksDownloaded = chunksDownloaded;
    }

    public long getChunksDownloaded() {
        return chunksDownloaded;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setChunksDownloaded(long chunksDownloaded) {
        this.chunksDownloaded = chunksDownloaded;
    }
}
