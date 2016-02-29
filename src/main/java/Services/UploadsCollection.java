package Services;

import models.UploadData;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadsCollection {

    List<UploadData> uploadDataList;
    String fileDir;

    public UploadsCollection(String fileDir) {
        uploadDataList = new ArrayList<>(5);
        this.fileDir = fileDir;
    }

    public UploadData addNew(UploadData uploadData){

        String newFileName = generateNewFileName(uploadData);
        uploadData.setAdditionalInformation(Paths.get(fileDir,newFileName),0);

        uploadDataList.add(uploadData);

        return uploadData;
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
        return uploadDataList.indexOf(uploadData) >= 0;
    }

    public boolean delete(UploadData uploadData){
        return uploadDataList.remove(uploadData);
    }
}
