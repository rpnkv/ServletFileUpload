package Services;

import models.DetailedUploadData;
import models.UploadData;
import upload.FileHandler;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadsCollection {

    List<DetailedUploadData> uploadDataList;

    public UploadsCollection() {
        uploadDataList = new ArrayList<>(5);
    }

    public void addNew(UploadData uploadData){

        String newFileName = generateNewFileName(uploadData);
        uploadDataList.add(new DetailedUploadData(uploadData, Paths.get(FileHandler.getFileDir(),newFileName),0));

    }

    private String generateNewFileName(UploadData uploadData){

        String username = uploadData.getUsername();
        String fileName = uploadData.getFileName();

        return username + "-" + new Date().getTime() + "-" + fileName;

    }

    public UploadData getContainingEqual(UploadData uploadData){
        return uploadDataList.get(uploadDataList.indexOf(uploadData));
    }

    public boolean contains(UploadData uploadData){
        return uploadDataList.indexOf(uploadData) > 0;
    }

    public boolean delete(UploadData uploadData){
        return uploadDataList.remove(uploadData);
    }
}
