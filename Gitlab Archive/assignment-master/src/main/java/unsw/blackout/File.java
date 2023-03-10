package unsw.blackout;

import unsw.response.models.FileInfoResponse;

class File {
    private String fileName;
    private String fileContent;
    private int fileSizeFull;

    public File(String fileName, String fileContent, int fileSizeFull) {
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.fileSizeFull = fileSizeFull;
    }

    public String getFileName() {
        return fileName;
    }
    
    public String getFileContent() {
        return fileContent;
    }

    public int getFileSize() {
        return fileContent.length();
    }

    public void resetFileSizeFull() {
        fileSizeFull = fileContent.length();
    }

    public void setFileContent(String content) {
        this.fileContent = content;
    }

    public void addFileContent(String content) {
        this.fileContent = (this.fileContent.concat(content));
    }

    public FileInfoResponse getInfo() {
        return new FileInfoResponse(fileName, fileContent, fileSizeFull, transferComplete());
    }

    public boolean transferComplete() {
        return getFileSize() == fileSizeFull;
    }
    
    public void removetBytes() {
        fileContent = fileContent.replace("t", "");
    }

}
