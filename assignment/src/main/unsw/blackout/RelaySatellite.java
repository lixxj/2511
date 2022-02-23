package unsw.blackout;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
    
    // CLASS ATTRIBUTES
    private static final double LINEAR_VELOCITY = 1.5E+3; 
    private static final double RANGE = 3E+5;
    private boolean direction = true;

    /**
     * CLASS CONSTRUCTOR - creates instance of Relay Satellite
     * 
     * @param satelliteId - unique satellite
     * @param type - type of satellite
     * @param height - height above Jupiter
     * @param position - angle position relative to Jupiter
     */
    public RelaySatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
    }

    /**
     * SUPERCLASS METHODS
     */
    
    public void moveSatellite() {
       double angle = super.getPosition().toDegrees();

       if (0 < angle && angle < 140 || angle > 345) {
            direction = true;
       } else if (190 < angle && angle <= 345) {
           direction = false;
       }

       super.moveSatellite(LINEAR_VELOCITY, direction);
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

}
