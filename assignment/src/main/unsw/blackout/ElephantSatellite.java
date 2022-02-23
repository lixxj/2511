package unsw.blackout;

import java.util.HashMap;
import java.util.Map;

import unsw.utils.Angle;

public class ElephantSatellite extends FileSharingSatellite  {
    
    // CLASS ATTRIBUTES
    private static final double LINEAR_VELOCITY = 2.5E+3; 
    private static final double RANGE = 4E+5;
    private static final boolean DIRECTION = true;
    private static final FileSharerSpecs FILE_SHARER_SPECS = new FileSharerSpecs(Integer.MAX_VALUE, 90, 20, 20);

    /**
     * CLASS CONSTRUCTOR - creates instance of Elephant Satellite
     * 
     * @param satelliteId - unique satellite
     * @param type - type of satellite
     * @param height - height above Jupiter
     * @param position - angle position relative to Jupiter
     */
    public ElephantSatellite(String deviceId, String type, double height, Angle position) {
        super(deviceId, type, height, position, FILE_SHARER_SPECS);
    }

    /**
     * SUPERCLASS METHODS
     */

    public void moveSatellite() {
        super.moveSatellite(LINEAR_VELOCITY, DIRECTION);
    }
    

    /**
     * CLASS METHODS
     */

    /**
     * Finds if the size of transient files is more than (or equal to) the required bytes that need to be freed
     * 
     * @param requiredBytes - number of bytes that need to be freed
     * @return - true if space can be freed (by deleting transient files) else false
     */
    public boolean canFreeSpace(int requiredBytes) {
        Map<File, Device> transientFiles = getTransientFiles();
        return (transientFiles.keySet().stream().map(file -> file.getFileSize()).reduce(0, Integer::sum) >= requiredBytes);
    }

    /**
     * Frees a given number of bytes from the current instance of ElephantSatellite
     * 
     * @param requiredBytes - number of bytes that need to be freed
     */
    public void freeSpace(int requiredBytes) {
        Map<File, Device> transientFiles = getTransientFiles();
        for (Map.Entry<File, Device> fileEntry : transientFiles.entrySet()) {
            if (requiredBytes <= 0) break;
            // remove file for satellite files (and any queues that it is in)
            super.removeFile(fileEntry.getKey().getFileName());
            requiredBytes -= fileEntry.getKey().getFileSize();
        }
    }

    /**
     * Gets a map of all transient files in the current instance of ElephantSatellite
     * @return 
     */
    private Map<File, Device> getTransientFiles() {
        Map<File, Device> transientFiles = new HashMap<>();
        for (Map.Entry<String, FileHandler> fileEntry : super.getDownloadQueue().entrySet()) {
            if (super.getFile(fileEntry.getKey()).isTransient()) transientFiles.put(super.getFile(fileEntry.getKey()), ((Device) fileEntry.getValue()));
        }
        return transientFiles;
    }

    /**
     * CONNECTABLE METHODS
     */

    public boolean canConnect(Object target) {
        if (!allowedToConnect(target)) return false;
        return super.canConnect(target, RANGE);
    }

    public boolean allowedToConnect(Object target) {
        return target instanceof Connectable && !(target instanceof Device && !(target instanceof DesktopDevice 
        || target instanceof LaptopDevice));
    }

}
