package unsw.blackout;

import java.util.HashMap;
import java.util.Map;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

abstract class StorageSatellite extends Satellite implements StorageEntity {
    private Map<String, File> Files;
    private Map<String, StorageEntity> uploadQueue;
    private Map<String, StorageEntity> downloadQueue;
    private int maxFiles;
    private int maxStorage;
    private int ReceiveSpeed;
    private int SendSpeed;

    public StorageSatellite(String Id, String type, double height, Angle position, int maxFiles, int maxStorage, int ReceiveSpeed, int SendSpeed) {
        super(Id, type, height, position);
        this.maxFiles = maxFiles;
        this.maxStorage = maxStorage;
        this.ReceiveSpeed = ReceiveSpeed;
        this.SendSpeed = SendSpeed;
        this.Files = new HashMap<>();
        this.uploadQueue = new HashMap<>();
        this.downloadQueue = new HashMap<>();  
    }

    public boolean hasFile(String fileName) {
        return Files.containsKey(fileName);
    }
    
    public File getFile(String fileName) {
        return Files.get(fileName);
    }

    public Map<String, FileInfoResponse> getFiles() {
        Map<String, FileInfoResponse> allFileInfoResponse = new HashMap<>();
        for (Map.Entry<String, File> fileEntry : Files.entrySet()) allFileInfoResponse.put(fileEntry.getKey(), fileEntry.getValue().getInfo());
        
        return allFileInfoResponse;
    }

    public void addFile(File file) {
        Files.put(file.getFileName(), file);
    }

    public void updateFile(String fileName, String newContent) {
        Files.get(fileName).addFileContent(newContent);
    }

    public void removeFile(String fileName) {
        if (uploadQueue.containsKey(fileName)) uploadQueue.remove(fileName);
        if (downloadQueue.containsKey(fileName)) downloadQueue.remove(fileName);
        Files.remove(fileName);
    }

    public int getUploadSpeed() {
        return SendSpeed;
    }
    
    public int getDownloadSpeed() {
        return ReceiveSpeed;
    }

    public Map<String, StorageEntity> getUploadQueue() {
        return uploadQueue;
    }

    public Map<String, StorageEntity> getDownloadQueue() {
        return downloadQueue;
    }
    
    public boolean canUpload() {
        return (SendSpeed - uploadQueue.size() > 0);
    }

    public boolean canDownload() {
        return (ReceiveSpeed - downloadQueue.size() > 0);
    }

    public int availableStorage() {
        int usedStorage = 0;
        for (File file : Files.values()) usedStorage += file.getFileSize();
        
        return (maxStorage - usedStorage);
    }

    public int availableFileSpace() {
        return (maxFiles - Files.size());
    }

    public void addToUploadQueue(String fileName, StorageEntity target) {
        uploadQueue.put(fileName, target);
    }

    public void addToDownloadQueue(String fileName, StorageEntity target) {
        downloadQueue.put(fileName, target);
    }
}
