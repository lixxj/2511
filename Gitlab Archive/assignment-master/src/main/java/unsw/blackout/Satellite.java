package unsw.blackout;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

abstract class Satellite implements Entity {
    private String Id;
    private String type;
    private double height;
    private Angle position;

    public Satellite(String Id, String type, double height, Angle position) {
        this.Id = Id;
        this.type = type;
        this.height = height;
        this.position = position;
    }

    public abstract void moveSatellite();
    public void moveSatellite(double linearVelocity, boolean direction) {
        Angle zero = Angle.fromDegrees(0);
        Angle full = Angle.fromDegrees(360);
        Angle newAngle = zero;
        
        if (direction) newAngle = Angle.fromRadians(position.add(Angle.fromRadians(linearVelocity / height)).toRadians());
        if (!direction) newAngle = Angle.fromRadians(position.subtract(Angle.fromRadians(linearVelocity / height)).toRadians());

        // Ensure position is in [0, 360)
        if (newAngle.toDegrees() <= zero.toDegrees()) newAngle = Angle.fromDegrees(newAngle.toDegrees() + 360);
        if (newAngle.toDegrees() >= full.toDegrees()) newAngle = Angle.fromDegrees(newAngle.toDegrees() - 360);

        position = newAngle;
    }

    public boolean connectable(Object target, double range) {
        if (!(target instanceof Entity)) {
            return false;
        }
        else if (target instanceof Device) {
            Device targetObject = (Device) target;
            return (MathsHelper.getDistance(height, position, targetObject.getPosition()) <= range && MathsHelper.isVisible(height, position, targetObject.getPosition()));
        } 
        else { // target instanceof Satellite
            Satellite targetObject = (Satellite) target;
            return (MathsHelper.getDistance(height, position, targetObject.getHeight(), targetObject.getPosition()) <= range && MathsHelper.isVisible(height, position, targetObject.getHeight(), targetObject.getPosition()));
        }
    }

    public String getSatelliteId() {
        return Id;
    }

    public String getType() {
        return type;
    }

    public double getHeight() {
        return height;
    }

    public Angle getPosition() { 
        return position;
    }

    public void setPosition(Angle newpos) { 
        position = newpos;
    }
}
