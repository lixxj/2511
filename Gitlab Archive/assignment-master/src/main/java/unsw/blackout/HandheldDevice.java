package unsw.blackout;

import unsw.utils.Angle;

class HandheldDevice extends Device {
    public HandheldDevice(String Id, Angle position) {
        super(Id, "HandheldDevice", position);
    }

    public boolean connectable(Object target) {
        // Handhelds have a range of only 50,000 kilometres (50,000,000 metres)
        return super.connectable(target, 50000);
    }
}
