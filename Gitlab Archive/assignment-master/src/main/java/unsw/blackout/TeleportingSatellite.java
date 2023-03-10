package unsw.blackout;

import unsw.utils.Angle;

class TeleportingSatellite extends StorageSatellite {

    private boolean direction = true; // Teleporting satellites start by moving anticlockwise
    
    public TeleportingSatellite(String Id, double height, Angle position) {
        // Can store up to 200 bytes and as many files as fits into that space
        // Can receive 15 bytes per minute and can send 10 bytes per minute
        super(Id, "TeleportingSatellite", height, position, 99999, 200, 15, 10);
    }

    public void moveSatellite() {
    
        Angle zero = Angle.fromDegrees(0);
        Angle half = Angle.fromDegrees(180);
        Angle full = Angle.fromDegrees(360);
        Angle newAngle = zero;
        Angle position = getPosition();
        double height = getHeight();

        // Moves at a linear velocity of 1,000 kilometres (1,000,000 metres) per minute
        if (direction) newAngle = Angle.fromRadians(position.add(Angle.fromRadians(1000 / height)).toRadians());
        if (!direction) newAngle = Angle.fromRadians(position.subtract(Angle.fromRadians(1000 / height)).toRadians());

        // Ensure position is in [0, 360)
        if (newAngle.toDegrees() < zero.toDegrees()) newAngle = Angle.fromDegrees(newAngle.toDegrees() + 360);
        if (newAngle.toDegrees() >= full.toDegrees()) newAngle = Angle.fromDegrees(newAngle.toDegrees() - 360);

        // When the position of the satellite reaches θ = 180, the satellite teleports to θ = 0 and changes direction
        if (!direction && (position.toDegrees() > half.toDegrees()) && (newAngle.toDegrees() <= half.toDegrees()) ) {
            newAngle = zero;
            direction = !direction;
        }
        else if (direction && (position.toDegrees() < half.toDegrees()) && (newAngle.toDegrees() >= half.toDegrees()) ) {
            newAngle = zero;
            direction = !direction;
        }

        setPosition(newAngle);
    }

    public boolean connectable(Object target) {
        return super.connectable(target, 200000); // Maximum range of 200,000 kilometres (200,000,000 metres)
    }

    public boolean allowedToConnect(Object target) {
        return target instanceof Entity; // Supports all devices
    }
}
