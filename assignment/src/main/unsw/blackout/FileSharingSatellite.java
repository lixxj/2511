package unsw.blackout;

import java.util.HashMap;
import java.util.Map;

import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public abstract class FileSharingSatellite extends Satellite implements FileSharer {
    
    // CLASS ATTRIBUTES
    private Map<String, File> allFiles;
    private FileSharerSpecs fileSharerSpecs;
    private Map<String, FileHandler> uploadQueue;
    private Map<String, FileHandler> downloadQueue;

    /**
     * CLASS CONSTRUCTOR - used by subclasses to specify satellite attributes
     * 
     * @param satelliteId - unique satellite
     * @param type - type of satellite
     * @param height - height above Jupiter
     * @param position - angle position relative to Jupiter
     * @param fileSharerSpecs - specifications required for File Sharing functionality (refer to 
     *                          FileSharerSpecs class)
     */
    public FileSharingSatellite(String satelliteId, String type, double height, Angle position, 
                                FileSharerSpecs fileSharerSpecs) {
        super(satelliteId, type, height, position);
        this.fileSharerSpecs = fileSharerSpecs;
        this.allFiles = new HashMap<>();
        this.uploadQueue = new HashMap<>();
        this.downloadQueue = new HashMap<>();
    }

    /**
     * SUPERCLASS METHODS
     */

    public void moveSatellite(double linearVelocity, Boolean anticlockwise) {
        super.moveSatellite(linearVelocity, anticlockwise);
    }

    public abstract void moveSatellite();

    /**
     * FILEHANDLER METHOD IMPLEMENTATIONS
     */
    
    public boolean containsFile(String fileName) {
        return allFiles.containsKey(fileName);
    }
    
    public File getFile(String fileName) {
        return allFiles.get(fileName);
    }

    public Map<String, FileInfoResponse> getFiles() {
        Map<String, FileInfoResponse> allFileInfos = new HashMap<>();
        for (Map.Entry<String, File> fileEntry : allFiles.entrySet()) {
            allFileInfos.put(fileEntry.getKey(), fileEntry.getValue().getInfo());
        }
        return allFileInfos;
    }

    public Map<String, File> getFileMap() {
        return allFiles;
    }

    public void addNewFile(File file) {
        allFiles.put(file.getFileName(), file);
    }

    public void addToExistingFile(String fileName, String newContent) {
        allFiles.get(fileName).addFileContent(newContent);
    }

    public void removeFile(String fileName) {
        allFiles.remove(fileName);
        if (uploadQueue.containsKey(fileName)) uploadQueue.remove(fileName);
        if (downloadQueue.containsKey(fileName)) downloadQueue.remove(fileName);
    }

    /**
     * FILESHARER METHOD IMPLEMENTATIONS
     */

    public Map<String, FileHandler> getUploadQueue() {
        return uploadQueue;
    }

    public Map<String, FileHandler> getDownloadQueue() {
        return downloadQueue;
    }
    
    public boolean canUpload() {
        return (fileSharerSpecs.getFileSendSpeed() - uploadQueue.size() > 0);
    }

    public boolean canDownload() {
        return (fileSharerSpecs.getFileReceiveSpeed() - downloadQueue.size() > 0);
    }

    public int getUploadSpeed() {
        return fileSharerSpecs.getFileSendSpeed();
    }
    public int getDownloadSpeed() {
        return fileSharerSpecs.getFileReceiveSpeed();
    }

    public int availableStorage() {
        int usedCapacity = 0;
        for (File file : allFiles.values()) {
            usedCapacity += file.getFileTrueSize();
        }
        return fileSharerSpecs.getMaxStorageCapacity() - usedCapacity;
    }

    public int availableFileSpace() {
        return fileSharerSpecs.getMaxFileCapacity() - allFiles.size();
    }

    public void queueFileUpload(String fileName, FileHandler receiver) {
        uploadQueue.put(fileName, receiver);
    }

    public void queueFileDownload(String fileName, FileHandler receiver) {
        downloadQueue.put(fileName, receiver);
    }

}
