package unsw.blackout;

import unsw.utils.Angle;

public class CloudStorageDevice extends DesktopDevice {

    /**
     * CLASS CONSTRUCTOR - creates instance of Desktop Device
     * 
     * @param deviceId - unique deviceId 
     * @param type - type of device 
     * @param position - angle position relative to Jupiter
     */
    public CloudStorageDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position);
    }
    
    /**
     * CONNECTABLE METHODS
     */
    public boolean canConnect(Object target) {
        return super.canConnect(target);
    }

}
