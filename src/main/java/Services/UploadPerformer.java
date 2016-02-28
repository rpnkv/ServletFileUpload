package Services;

import enums.DataChunkSavingResult;
import models.DetailedUploadData;
import models.UploadData;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UploadPerformer {


    public void appendNextDataChunkRequestToResponse(UploadData abstractUploadData, HttpServletResponse response)
            throws IOException, ClassCastException {
        try {
            DetailedUploadData uploadData = (DetailedUploadData) abstractUploadData;

            long nextChunkStartIndex = uploadData.getChunksDownloaded() * uploadData.getChunkSize(),
                    fileSize = uploadData.getSize(),
                    nextChunkEndIndex;

            if (nextChunkStartIndex + uploadData.getChunkSize() < fileSize) {
                nextChunkEndIndex = nextChunkStartIndex + uploadData.getChunkSize();
            } else {
                nextChunkEndIndex = nextChunkStartIndex + (fileSize - nextChunkStartIndex);
            }

            JSONOperator.appendMapAsJSONToResponse(response, uploadDataToMap(nextChunkStartIndex, nextChunkEndIndex, "uploading"));

        } catch (ClassCastException e) {
            response.sendError(503, "Upload data is corrupted. Cannot continue file upload.");
        }
    }

    private Map<String, String> uploadDataToMap(long nextChunkStartIndex, long nextChunkEndIndex, String uploadStatus) {
        Map<String, String> values = new HashMap<>(6);
        values.put("uploadStatus", uploadStatus);
        values.put("nextChunkStartIndex", String.valueOf(nextChunkStartIndex));
        values.put("nextChunkEndIndex", String.valueOf(nextChunkEndIndex));

        return values;
    }

    public DataChunkSavingResult saveDataChunk(Part dataChunk, UploadData abstractUploadData) throws ClassCastException {
        try {
            byte[] incomingBytes = getIncomingBytes(dataChunk);

            DetailedUploadData uploadData = (DetailedUploadData) abstractUploadData;

            saveBytesToFile(incomingBytes,uploadData.getPath().toFile());

            uploadData.incDownloadedChunksCounter();

            return uploadData.allChunksAreUploaded() ? DataChunkSavingResult.fileUploaded : DataChunkSavingResult.chunkSaved;

        }catch (IOException ex){
            return DataChunkSavingResult.ioError;
        }catch (ClassCastException ex){
            return DataChunkSavingResult.descriptionError;
        }
    }

    private byte[] getIncomingBytes(Part dataChunk) throws IOException {

        InputStream is = dataChunk.getInputStream();

        byte[] incomingBytes = new byte[is.available()];

        is.read(incomingBytes);

        is.close();

        return incomingBytes;
    }

    private void saveBytesToFile(byte[] bytes, File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file,true);
        fos.write(bytes);
        fos.close();
    }

    public void resetUploadDataIfFileWasUploaded(UploadData abstractUploadData) throws ClassCastException, IOException {
        DetailedUploadData uploadData = (DetailedUploadData) abstractUploadData;

        if(uploadData.allChunksAreUploaded()){
            File fileToErase = uploadData.getPath().toFile();
            fileToErase.delete();
            fileToErase.createNewFile();
            uploadData.resetChunksCounter();
        }

    }

    public void appendSuccessUploadMessageToResponse(HttpServletResponse response) {
        Map<String,String> params = new HashMap<>(4);
        params.put("uploadStatus","uploaded");

        JSONOperator.appendMapAsJSONToResponse(response,params);
    }
}
