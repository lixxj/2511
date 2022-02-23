package unsw.blackout;

import java.util.Map;

import unsw.response.models.FileInfoResponse;

public interface FileHandler {
    
    /**
     * Checks if the instantiated FileHandler object contains a specific file.
     * 
     * @param fileName - name of file (unique on blackout system for ALL files)
     * @return true if file exists else false
     */
    public boolean containsFile(String fileName);

    /**
     * @param fileName - name of file (unique on blackout system for ALL files)
     * @return specified file from the stored files on the instantiated FileHandler
     *          object
     */
    public File getFile(String fileName);

    /**
     * Get detailed information for all the files that currently exist on the
     * instantiated FileHandler object.
     * 
     * @return map for each file's name and it's information response object
     */
    public Map<String, FileInfoResponse> getFiles();

    /**
     * @return map containing all files currently stored on the instantiated
     *          FileHandler object
     */
    public Map<String, File> getFileMap();

    /**
     * Adds a specific file to the instantiated FileHandler object's file storage.
     * 
     * @param file - file that will be added
     */
    public void addNewFile(File file);

    /**
     * Assuming that the specified file exists in the instantiated FileHandler
     * object's file storage, append content to the file.
     * 
     * @param fileName - name of file (unique on blackout system for ALL files)
     * @param newContent - content that will be appended to specified file
     */
    public void addToExistingFile(String fileName, String newContent);

    /**
     * Removes file from the instantiated FileHandler object's file storage.
     * 
     * @param fileName - name of file (unique on blackout system for ALL files)
     */
    public void removeFile(String fileName);

}
