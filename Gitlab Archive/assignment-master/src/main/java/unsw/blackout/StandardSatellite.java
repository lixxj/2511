package unsw.blackout;

import unsw.utils.Angle;

class StandardSatellite extends StorageSatellite {
    
    public StandardSatellite(String Id, double height, Angle position) {
        // Can store up to either 3 files or 80 bytes (whichever is smallest for the current situation)
        // Can receive 1 byte per minute and can send 1 byte per minute meaning it can only transfer 1 file at a time
        super(Id, "StandardSatellite", height, position, 3, 80, 1, 1);
    }

    public void moveSatellite() {
        // Moves at a linear speed of 2,500 kilometres (2,500,000 metres) per minute
        // Default direction for all satellites is negative (clockwise)
        super.moveSatellite(2500, false);
    }

    public boolean connectable(Object target) {
        if (!allowedToConnect(target)) return false;
        // Maximum range of 150,000 kilometres (150,000,000 metres)
        return super.connectable(target, 150000);
    }

    public boolean allowedToConnect(Object target) {
        // Supports handhelds and laptops only (along with other satellites)
        return target instanceof Entity && !(target instanceof Device && !(target instanceof HandheldDevice || target instanceof LaptopDevice));
    }
}
