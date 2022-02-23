package unsw.blackout;

import unsw.utils.Angle;

public class StandardSatellite extends FileSharingSatellite  {
    
    // CLASS ATTRIBUTES
    private static final double LINEAR_VELOCITY = 2.5E+3; 
    private static final double RANGE = 1.5E+5;
    private static final boolean DIRECTION = true;
    private static final FileSharerSpecs FILE_SHARER_SPECS = new FileSharerSpecs(3, 80, 1, 1);

    /**
     * CLASS CONSTRUCTOR - creates instance of Standard Satellite
     * 
     * @param satelliteId - unique satellite
     * @param type - type of satellite
     * @param height - height above Jupiter
     * @param position - angle position relative to Jupiter
     */
    public StandardSatellite(String deviceId, String type, double height, Angle position) {
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
        if (!allowedToConnect(target)) return false;
        return super.canConnect(target, RANGE);
    }

    public boolean allowedToConnect(Object target) {
        return target instanceof Connectable && !(target instanceof Device && !(target instanceof HandheldDevice 
        || target instanceof LaptopDevice));
    }
}
