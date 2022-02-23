package unsw.blackout;

import unsw.utils.Angle;

public class HandheldDevice extends Device {

    // CLASS ATTRIBUTES
    private static final double RANGE = 5E+4;

    /**
     * CLASS CONSTRUCTOR - creates instance of Handheld Device
     * 
     * @param deviceId - unique deviceId 
     * @param type - type of device 
     * @param position - angle position relative to Jupiter
     */
    public HandheldDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position);
    }

    /**
     * CONNECTABLE METHODS
     */
    
    public boolean canConnect(Object target) {
        return super.canConnect(target, RANGE);
    }

}
