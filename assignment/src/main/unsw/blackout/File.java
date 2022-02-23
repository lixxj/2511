package unsw.blackout;

import unsw.response.models.FileInfoResponse;

public class File {
    
    // CLASS ATTRIBUTES
    private String fileName;
    private String fileContent;
    private int trueFileSize;
    private boolean isTransient;

    /**
     * CLASS CONSTRUCTOR - creates an instance of File
     * 
     * @param fileName - name of file (unique on blackout system for ALL files)
     * @param fileContent - content inside of file
     * @param trueFileSize - size of the full file
     */
    public File(String fileName, String fileContent, int trueFileSize) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.trueFileSize = trueFileSize;
        this.isTransient = false;
    }

    /**
     * GETTER AND SETTER METHODS
     */

    /**
     * @return InfoResponse object containing basic information about file 
     *          (name, file content, full file size, transfer status)
     */
    public FileInfoResponse getInfo() {
        return new FileInfoResponse(getFileName(), getFileContent(), 
                                    getFileTrueSize(), transferComplete());
    }

    /**
     * @return true if the file is a transient file (out of range but will 
     *          continue downloading when in range) else false
     */
    public boolean isTransient() {
        return isTransient;
    }

    /**
     * Changes the transient status based on the boolean provided
     * @param isTransient - true if file is transient (out of range but 
     *          will continue downloading when in range) else false
     */
    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    /**
     * @return name of file (unique on blackout system for ALL files)
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return current size of the file content (measured in bytes)
     */
    public int getFileSize() {
        return fileContent.length();
    }

    /**
     * @return size of the fully transferred file (measured in bytes)
     */
    public int getFileTrueSize() {
        return trueFileSize;
    }

    /**
     * @return file contents (i.e. text inside of file)
     */
    public String getFileContent() {
        return fileContent;
    }

    /**
     * @param content - new content that will replace current file contents 
     */
    public void setFileContent(String content) {
        this.fileContent = content;
    }

    /**
     * @param content - appends content to existing file content
     */
    public void addFileContent(String content) {
        setFileContent(this.fileContent.concat(content));
    }

    /**
     * @return checks if current file size is the same as the size of the 
     *          fully transferred file
     */
    public boolean transferComplete() {
        return getFileSize() == trueFileSize;
    }

}
