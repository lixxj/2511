package unsw.blackout;

import java.util.HashMap;
import java.util.Map;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

abstract class Device implements StorageEntity {
    private String Id;
    private String type;
    private Angle position;
    private Map<String, File> Files;

    public Device(String Id, String type, Angle position) {
        this.Id = Id;
        this.type = type;
        this.position = position;
        this.Files = new HashMap<>();
    }

    public boolean connectable(Object target, double range) {
        if (target == null || !allowedToConnect(target)) return false;
        Satellite targetObject = (Satellite) target;
        
        return (MathsHelper.getDistance(targetObject.getHeight(), targetObject.getPosition(), position) <= range && MathsHelper.isVisible(targetObject.getHeight(), targetObject.getPosition(), position));
    }

    public boolean allowedToConnect(Object target) {
        return target instanceof Satellite;
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
        Files.remove(fileName);
    }

    public String getDeviceId() {
        return Id;
    }
 
    public String getType() {
        return type;
    }

    public Angle getPosition() {
        return position;
    }
}
