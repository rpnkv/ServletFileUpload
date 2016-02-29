package models;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.security.InvalidParameterException;

public class UploadData {

    protected String username;
    protected String fileName;
    protected long size;
    protected long modifyDate;

    Path path;
    long chunksDownloaded = 0;
    private int chunkSize = 1024 * 1024 * 8;

    public UploadData(String username, String fileName, long size, long modifyDate) {
        this.username = username;
        this.fileName = fileName;
        this.size = size;
        this.modifyDate = modifyDate;
    }



    public static UploadData createFileIdByRequest(HttpServletRequest request) throws InvalidParameterException{
        String username = request.getParameter("username");
        String filename = request.getParameter("fileName");

        checkStringParameters(username,filename);

        long fileSize;
        long fileModifyDate;

        try {
            fileSize= Long.parseLong(request.getParameter("fileSize"));
            fileModifyDate = Long.parseLong(request.getParameter("fileModifyDate"));
        }catch (NumberFormatException ex){
            throw new InvalidParameterException("some required params have not been received");
        }

        return new UploadData(username,filename,fileSize,fileModifyDate);
    }

    private static void checkStringParameters(String username, String fileName) throws InvalidParameterException{
        if(username == null)
            throw new InvalidParameterException("no username set");

        if(fileName == null)
            throw new InvalidParameterException("no filename set");
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof UploadData))
            return false;

        UploadData comparableObject = (UploadData) obj;

        return (comparableObject.username.equals(username) &&
                comparableObject.fileName.equals(fileName) &&
                (comparableObject.size == size) &&
                (modifyDate == comparableObject.modifyDate)
        );
    }

    public String getUsername() {
        return username;
    }

    public String getFileName() {
        return fileName;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }







    public void setAdditionalInformation(Path path, long chunksDownloaded) {
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

