package unsw.blackout;

import unsw.utils.Angle;

public class LaptopDevice extends Device {

    // CLASS ATTRIBUTES
    private static final double RANGE = 1E+5;

    /**
     * CLASS CONSTRUCTOR - creates instance of LaptopDevice
     * 
     * @param deviceId - unique deviceId 
     * @param type - type of device 
     * @param position - angle position relative to Jupiter
     */
    public LaptopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position);
    }

    /**
     * CONNECTABLE METHODS
     */
    
    public boolean canConnect(Object target) {
        return super.canConnect(target, RANGE);
    }

}
