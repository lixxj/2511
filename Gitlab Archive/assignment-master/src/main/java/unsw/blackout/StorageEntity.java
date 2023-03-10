package unsw.blackout;

import java.util.Map;
import unsw.response.models.FileInfoResponse;

interface StorageEntity extends Entity { // The Entity that is able to manage files
    public boolean hasFile(String fileName);

    public File getFile(String fileName);

    public Map<String, FileInfoResponse> getFiles();

    public void addFile(File file);

    public void updateFile(String fileName, String newContent);

    public void removeFile(String fileName);
}
