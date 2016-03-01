package uploader.models;

import uploader.exceptions.InvalidParameterException;

import javax.servlet.http.HttpServletRequest;

public class UploadId {

    private String username;
    private String fileName;
    private long size;
    private long modifyDate;

    public UploadId(String username, String fileName, long size, long modifyDate) {
        this.username = username;
        this.fileName = fileName;
        this.size = size;
        this.modifyDate = modifyDate;
    }

    public static UploadId createFileIdByRequest(HttpServletRequest request) throws InvalidParameterException {
        String username = request.getParameter("username");
        if(username == null){
            throw new InvalidParameterException("username");
        }

        String filename = request.getParameter("fileName");
        if(filename == null){
            throw new InvalidParameterException("filename");
        }

        long fileSize;
        long fileModifyDate;

        try {
            fileSize = Long.parseLong(request.getParameter("fileSize"));
            fileModifyDate = Long.parseLong(request.getParameter("fileModifyDate"));
        }catch (NumberFormatException ex){
            throw new InvalidParameterException(ex);
        }

        return new UploadId(username,filename,fileSize,fileModifyDate);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof UploadId))
            return false;

        UploadId comparableObject = (UploadId) obj;

        return (comparableObject.username.equals(username) &&
                comparableObject.fileName.equals(fileName) &&
                (comparableObject.size == size) &&
                (modifyDate == comparableObject.modifyDate)
        );
    }

}
