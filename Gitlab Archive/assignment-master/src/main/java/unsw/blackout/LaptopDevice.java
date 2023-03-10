package unsw.blackout;

import unsw.utils.Angle;

class LaptopDevice extends Device {
    public LaptopDevice(String Id, Angle position) {
        super(Id, "LaptopDevice", position);
    }

    public boolean connectable(Object target) {
        // Laptops have a range of only 100,000 kilometres (100,000,000 metres)
        return super.connectable(target, 100000);
    }
}
