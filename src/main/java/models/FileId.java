package models;

public class FileId {

    private String username;
    private String filename;
    private long size;
    private long modifyDate;

    public FileId(String username, String filename, long size, long modifyDate) {
        this.username = username;
        this.filename = filename;
        this.size = size;
        this.modifyDate = modifyDate;
    }

    public String getUsername() {
        return username;
    }

    public String getFilename() {
        return filename;
    }

    public long getSize() {
        return size;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }
}
