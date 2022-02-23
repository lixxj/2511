package unsw.blackout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Device implements Connectable, FileHandler {

    // DEVICE ATTRIBUTES
    private String deviceID;
    private String type;
    private Angle position;
    private Map<String, File> allFiles;

    /**
     * CLASS CONSTRUCTOR - used by subclasses to specify device attributes
     * 
     * @param deviceId - unique deviceId 
     * @param type - type of device 
     * @param position - angle position relative to Jupiter
     */
    public Device(String deviceID, String type, Angle position) {
        this.deviceID = deviceID;
        this.type = type;
        this.position = position;
        this.allFiles = new HashMap<>();
    }

    /**
     * CONNECTABLE METHODS
     */

    public boolean canConnect(Object target, double range) {
        if (target == null || !allowedToConnect(target)) return false;
        Satellite targetObject = (Satellite) target;
        return (MathsHelper.getDistance(targetObject.getHeight(), targetObject.getPosition(), position) <= range 
            && MathsHelper.isVisible(targetObject.getHeight(), targetObject.getPosition(), position));
    }

    public boolean allowedToConnect(Object target) {
        return target instanceof Satellite;
    }

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
    }

    /**
     * GETTER AND SETTER METHODS
     */

    /**
     * @return unique device ID
     */
    public String getDeviceID() {
        return deviceID;
    }
 
    /**
     * @return type of device (i.e. Handheld, Laptop, Desktop, CloudStorage etc.)
     */
    public String getType() {
        return type;
    }

    /**
     * @return angle position relative to Jupiter
     */
    public Angle getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Device)) return false;

        Device other = (Device) obj;
        return Objects.equals(deviceID, other.getDeviceID());
    }

}
