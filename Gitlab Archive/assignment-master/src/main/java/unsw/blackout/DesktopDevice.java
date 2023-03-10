package unsw.blackout;

import unsw.utils.Angle;

class DesktopDevice extends Device {
    public DesktopDevice(String Id, Angle position) {
        super(Id, "DesktopDevice", position);
    }
    
    public boolean connectable(Object target) {
        // Desktops have a range of only 200,000 kilometres (200,000,000 metres)
        return super.connectable(target, 200000);
    }
}
