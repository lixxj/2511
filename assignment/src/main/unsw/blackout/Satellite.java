package unsw.blackout;

import java.util.Objects;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Satellite implements Connectable {
    
    // CLASS ATTRIBUTES
    private String satelliteId;
    private String type;
    private double height;
    private Angle position;


    /**
     * CLASS CONSTRUCTOR - used by subclasses to specify satellite attributes
     * 
     * @param satelliteId - unique satellite
     * @param type - type of satellite
     * @param height - height above Jupiter
     * @param position - angle position relative to Jupiter
     */
    public Satellite(String satelliteId, String type, double height, Angle position) {
        this.satelliteId = satelliteId;
        this.type = type;
        this.height = height;
        this.position = position;
    }

    /**
     * CLASS METHODS
     */
    
    /**
     * Moves the instantiated satellite (i.e. changes its position in the blackout system)
     * 
     * @param linearVelocity - velocity in which the satellite travels (measured in km/min)
     * @param anticlockwise - true if direction of travel is anticlockwise
     */
    public void moveSatellite(double linearVelocity, Boolean anticlockwise) {
        if (anticlockwise) {
            position = Angle.fromRadians(position.add(Angle.fromRadians(linearVelocity / height)).toRadians() % (2*Math.PI));
        } else {
            position = Angle.fromRadians(position.subtract(Angle.fromRadians(linearVelocity / height)).toRadians() % (2*Math.PI));
        }
    }

    /**
     * Abstract method that enables all subclasses to call the superclass's 
     * method by specifying the linear velocity and the direction in which
     * the satellite travels. Also enables subclasses to specify constraints
     * on the movement. 
     */
    public abstract void moveSatellite();

    /**
     * CONNECTABLE METHODS
     */

    public boolean canConnect(Object target, double range) {
        if (target == null || !(target instanceof Connectable)) return false;
        if (target instanceof Device) {
            Device targetObject = (Device) target;
            return (MathsHelper.getDistance(height, position, targetObject.getPosition()) <= range 
            && MathsHelper.isVisible(height, position, targetObject.getPosition()));
        } else { // target instanceof Satellite
            Satellite targetObject = (Satellite) target;
            return (MathsHelper.getDistance(height, position, targetObject.getHeight(), targetObject.getPosition()) <= range 
                && MathsHelper.isVisible(height, position, targetObject.getHeight(), targetObject.getPosition()));
        }
    }

    /**
     * GETTER AND SETTER METHODS
     */

    /**
     * @return unique Satellite ID 
     */
    public String getSatelliteId() {
        return satelliteId;
    }

    /**
     * @return type of satellite (i.e. Standard, Shrinking, Relay, Elephant etc.)
     */
    public String getType() {
        return type;
    }

    /**
     * @return height of satellite above the planet
     */
    public double getHeight() {
        return height;
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
        if (obj == null || !(obj instanceof Satellite)) return false;

        Satellite other = (Satellite) obj;
        return Objects.equals(satelliteId, other.getSatelliteId());
    }

}
