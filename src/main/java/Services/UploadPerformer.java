package Services;

import models.UploadData;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UploadPerformer {


    public void appendNextDataChunkRequestToResponse(UploadData uploadData, HttpServletResponse response) throws IOException {

        long nextChunkStartIndex = uploadData.getChunksDownloaded() * uploadData.getChunkSize(),
                nextChunkEndIndex = defineNextChunkEndIndex(nextChunkStartIndex, uploadData);

        JSONOperator.appendMapAsJSONToResponse(response, uploadDataToMap(nextChunkStartIndex, nextChunkEndIndex, "uploading"));

    }

    private long defineNextChunkEndIndex(long nextChunkStartIndex, UploadData uploadData) {
        return (nextChunkStartIndex + uploadData.getChunkSize() < uploadData.getSize()) ?
                nextChunkStartIndex + uploadData.getChunkSize() :
                nextChunkStartIndex + (uploadData.getSize() - nextChunkStartIndex);

    }

    private Map<String, String> uploadDataToMap(long nextChunkStartIndex, long nextChunkEndIndex, String uploadStatus) {
        Map<String, String> values = new HashMap<>(6);
        values.put("uploadStatus", uploadStatus);
        values.put("nextChunkStartIndex", String.valueOf(nextChunkStartIndex));
        values.put("nextChunkEndIndex", String.valueOf(nextChunkEndIndex));

        return values;
    }

    //returns false if file wasn't uploaded completely
    public boolean trySaveDataChunk(Part dataChunk, UploadData uploadData) throws IOException {

        return saveDataChunk(dataChunk, uploadData);

    }

    private boolean saveDataChunk(Part dataChunk, UploadData uploadData) throws IOException {
        byte[] incomingBytes = getIncomingBytes(dataChunk);

        saveBytesToFile(incomingBytes, uploadData.getPath().toFile());

        uploadData.incDownloadedChunksCounter();

        return uploadData.allChunksAreUploaded();
    }

    private byte[] getIncomingBytes(Part dataChunk) throws IOException {

        InputStream is = dataChunk.getInputStream();

        byte[] incomingBytes = new byte[is.available()];

        is.read(incomingBytes);

        is.close();

        return incomingBytes;
    }

    private void saveBytesToFile(byte[] bytes, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(bytes);
        fos.close();
    }

    public void resetUploadDataIfFileWasUploaded(UploadData uploadData) throws IOException {

        if (uploadData.allChunksAreUploaded()) {
            File fileToErase = uploadData.getPath().toFile();
            fileToErase.delete();
            fileToErase.createNewFile();
            uploadData.resetChunksCounter();
        }

    }

    public void appendSuccessUploadMessageToResponse(HttpServletResponse response) throws IOException {
        Map<String, String> params = new HashMap<>(4);
        params.put("uploadStatus", "uploaded");

        JSONOperator.appendMapAsJSONToResponse(response, params);
    }
}
