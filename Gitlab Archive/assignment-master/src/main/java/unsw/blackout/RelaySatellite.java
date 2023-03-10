package unsw.blackout;

import unsw.utils.Angle;

class RelaySatellite extends Satellite {
    private boolean direction = false;

    public RelaySatellite(String Id, double height, Angle position) {
        super(Id, "RelaySatellite", height, position);
    }

    public void moveSatellite() {
        double position = super.getPosition().toDegrees();

        // Only travels in the region between 140° and 190°
        // When it reaches one side of the region its direction reverses and it travels in the opposite direction
        if (0 < position && position < 140 || position >= 345) direction = true;
        if (190 < position && position < 345) direction = false;

        // Moves at a linear velocity of 1,500 kilometres (1,500,000 metres) per minute
        super.moveSatellite(1500, direction);
    }

    public boolean connectable(Object target) {
        // Max range of 300,000 kilometres (300,000,000 metres)
        return super.connectable(target, 300000);
    }

    public boolean allowedToConnect(Object target) {
        // Supports all devices
        return target instanceof Entity;
    }
}
