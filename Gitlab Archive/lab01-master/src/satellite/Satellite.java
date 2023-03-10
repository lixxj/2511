package satellite;


public class Satellite {
    
    // Fields for Satellite
    private String name;
    private int height;
    private double rposition;
    private double dposition;
    private double lvelocity;
    //private double avelocity;
    
    /**
     * Constructor for Satellite
     * @param name
     * @param height
     * @param velocity
     */
    public Satellite(String name, int height, double position, double velocity) {
        this.name = name;
        this.height = height;
        this.dposition = position;
        this.rposition = Math.toRadians(position);
        this.lvelocity = velocity;
        //this.avelocity = velocity / height; // can not calculate it here because it will not change when height change
    }

    // Methods for Satellite
    /**
     * Getter for name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for position (degrees)
     */
    public double getPositionDegrees() {
        return dposition;
    }

    /**
     * Getter for position (radians)
     */
    public double getPositionRadians() {
        return rposition;
    }

    /**
     * Returns the linear velocity (metres per second) of the satellite
     */
    public double getLinearVelocity() {
        return lvelocity;
    }

    /**
     * Returns the angular velocity (radians per second) of the satellite
     */
    public double getAngularVelocity() {
        return lvelocity / height;
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
        this.lvelocity = velocity;
    }

    /**
     * Setter for position
     * @param position
     */
    public void setPosition(double position) {
        this.dposition = position;
    }

    /**
     * Calculates the distance travelled by the satellite in the given time
     * @param time (seconds)
     * @return distance in metres
     */
    public double distance(double time) {
        return time * lvelocity;
    }

    public static void main(String[] args) {
        // Creates Satellite A, which is 10000 km above the centre of the earth, with a velocity of 55 metres per second and at θ = 122
        Satellite A = new Satellite("Satellite A", 10000, 122.0, 55.0);
        // Creates Satellite B, which is 5438 km above the centre of the earth, with a velocity of 234 kilometres per second and at θ = 0
        Satellite B = new Satellite("Satellite B", 5438, 0.0, 234.0);
        // Creates Satellite C, which is 9029 km above the centre of the earth, with a velocity of 0 metres per second and at θ = 284
        Satellite C = new Satellite("Satellite C", 9029, 284.0, 0.0);
        // For Satellite A, print out I am {name} at position {theta} degrees, {height} km above the centre of the earth and moving at a velocity of {velocity} metres per second
        System.out.println("I am " + A.getName() + " at position " + A.getPositionDegrees() + " degrees, " + A.getHeight() + " km above the centre of the earth and moving at a velocity of " + A.getLinearVelocity() + " metres per second");
        // Change Satellite A's height to 9999
        A.setHeight(9999);
        // Change Satellite B's angle to 45
        B.setPosition(45);
        // Change Satellite C's velocity to 36.5 mps
        C.setVelocity(36.5);
        // Print out Satellite A's position in radians
        System.out.println(A.getPositionRadians());
        // Print out Satellite B's angular velocity
        System.out.println(B.getAngularVelocity());
        // Print out the distance Satellite C travels after 2 minutes
        System.out.println(C.distance(120));
    }
}