package satellite;


public class Satellite {
    
    private String name;
    private int height;
    private double position;
    private double velocity;

    /**
     * Constructor for Satellite
     * @param name
     * @param height
     * @param velocity
     */
    public Satellite(String name, int height, double position, double velocity) {
        setName(name);
        setHeight(height);
        setPosition(position);
        setVelocity(velocity);
    }

    /**
     * Getter for name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Getter for position (degrees)
     */
    public double getPositionDegrees() {
        return this.position;
    }
    
    /**
     * Getter for position (radians)
     */
    public double getPositionRadians() {
        return Math.toRadians(this.position);
    }

    /**
     * Returns the linear velocity (metres per second) of the satellite
     */
    public double getLinearVelocity() {
        return this.velocity;
    }

    /**
     * Returns the angular velocity (degrees per second) of the satellite
     */
    public double getAngularVelocity() {
        return this.velocity / (this.height * 1000);
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for height
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Setter for velocity
     * @param velocity
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Setter for position
     * @param position
     */
    public void setPosition(double position) {
        this.position = position;
    }

    /**
     * Calculates the distance travelled by the satellite in the given time
     * @param time (seconds)
     * @return distance in metres
     */
    public double distance(double time) {
        return this.velocity * time;
    }

    public static void main(String[] args) {
        Satellite sat_A = new Satellite("Satellite A", 10000, 122, 55);
        Satellite sat_B = new Satellite("Satellite B", 5438, 0, 234_000);
        Satellite sat_C = new Satellite("Satellite C", 9029, 284, 0);

        System.out.printf("I am %s at position %.1f degrees, %d km above the centre of the earth and moving at a velocity of %.1f metres per second%n", sat_A.getName(), sat_A.getPositionDegrees(), sat_A.getHeight(), sat_A.getLinearVelocity());

        sat_A.setHeight(9999);
        sat_B.setPosition(45);
        sat_C.setVelocity(36.5);

        System.out.println(sat_A.getPositionRadians());
        System.out.println(sat_B.getAngularVelocity());
        System.out.println(sat_C.distance(120));
    }

}