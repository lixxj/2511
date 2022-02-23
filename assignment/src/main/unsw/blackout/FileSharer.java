package unsw.blackout;

import java.util.Map;

public interface FileSharer extends FileHandler {
    
    /**
     * An upload queue stores all files that have been scheduled to be sent
     * to a given FileHandler object.
     * 
     * @return Map containing all files and their respective receipients.
     */
    public Map<String, FileHandler> getUploadQueue();

    /**
     * An download queue stores all files that have been scheduled to be 
     * downloaded by a given FileHandler object.
     * 
     * @return Map containing all files and their respective senders.
     */
    public Map<String, FileHandler> getDownloadQueue();

    /**
     * Checks whether the current bandwidth of a FileSharing object can
     * support another file upload.
     * @return true if another file can be uploaded else false
     */
    public boolean canUpload();

    /**
     * Checks whether the current bandwidth of a FileSharing object can
     * support another file download.
     * @return true if another file can be downloaded else false
     */
    public boolean canDownload();

    /**
     * @return maximum upload bandwidth of a given FileSharing object (bytes/min)
     */
    public int getUploadSpeed();

    /**
     * @return maximum download bandwidth of a given FileSharing object (bytes/min)
     */
    public int getDownloadSpeed();
    
    /**
     * @return available file storage that accounts for all stored files as well as
     *          the true sizes of the files that are queued for download (if any)
     */
    public int availableStorage();

    /**
     * @return number of files that can be stored on this FileSharing object 
     *          currently
     */
    public int availableFileSpace();

    /**
     * Adds a given file (assuming that it exists on the current FileSharing object)
     * to the queue of files that are scheduled to be uploaded.
     * 
     * @param fileName - name of file (unique on blackout system for ALL files)
     * @param receiver - recipient of given file
     */
    public void queueFileUpload(String fileName, FileHandler receiver);

    /**
     * Adds a given file (assuming that it exists on the specified sender object)
     * to the queue of files that are scheduled to be downloaded.
     * 
     * @param fileName - name of file (unique on blackout system for ALL files)
     * @param sender - sender of given file
     */
    public void queueFileDownload(String fileName, FileHandler sender);

}
