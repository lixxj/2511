package unsw.blackout;

import java.util.HashMap;
import java.util.Map;

import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class ShrinkingSatellite extends FileSharingSatellite {

    // CLASS ATTRIBUTES
    private static final double LINEAR_VELOCITY = 1E+3; 
    private static final double RANGE = 2E+5;
    private static final boolean DIRECTION = true;
    private static final FileSharerSpecs FILE_SHARER_SPECS = new FileSharerSpecs(Integer.MAX_VALUE, 150, 15, 10);
    
    /**
     * CLASS CONSTRUCTOR - creates instance of Relay Satellite
     * 
     * @param satelliteId - unique satellite
     * @param type - type of satellite
     * @param height - height above Jupiter
     * @param position - angle position relative to Jupiter
     */
    public ShrinkingSatellite(String deviceId, String type, double height, Angle position) {
        super(deviceId, type, height, position, FILE_SHARER_SPECS);
    }

    /**
     * SUPERCLASS METHODS
     */

    public void moveSatellite() {
        super.moveSatellite(LINEAR_VELOCITY, DIRECTION);
    }

    /**
     * CONNECTABLE METHODS
     */

    public boolean canConnect(Object target) {
        return super.canConnect(target, RANGE);
    }

    public boolean allowedToConnect(Object target) {
        return target instanceof Connectable;
    }

    /**
     * FILEHANDLER METHODS
     */

    @Override
    public Map<String, FileInfoResponse> getFiles() {
        Map<String, FileInfoResponse> allFileInfos = new HashMap<>();
        for (Map.Entry<String, File> fileEntry : super.getFileMap().entrySet()) {
            int size = fileEntry.getValue().getFileTrueSize();
            if (fileEntry.getValue().getFileContent().contains("quantum") && fileEntry.getValue().transferComplete()) size *= ((double) 2/3);
            allFileInfos.put(fileEntry.getKey(), new FileInfoResponse(fileEntry.getValue().getFileName(), 
            fileEntry.getValue().getFileContent(), size, fileEntry.getValue().transferComplete()));
        }
        return allFileInfos;
    }
}
