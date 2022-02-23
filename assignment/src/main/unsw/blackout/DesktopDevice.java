package unsw.blackout;

import unsw.utils.Angle;

public class DesktopDevice extends Device {

    // CLASS ATTRIBUTES
    private static final double RANGE = 2E+5;

    /**
     * CLASS CONSTRUCTOR - creates instance of Desktop Device
     * 
     * @param deviceId - unique deviceId 
     * @param type - type of device 
     * @param position - angle position relative to Jupiter
     */
    public DesktopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position);
    }
    
    /**
     * CONNECTABLE METHODS
     */
    public boolean canConnect(Object target) {
        return super.canConnect(target, RANGE);
    }

}
