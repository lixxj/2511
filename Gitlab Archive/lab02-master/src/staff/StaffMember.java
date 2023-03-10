/*
Modify the class StaffMember (inside package staff) so that it satisfies the following requirements:

Attributes to store the staff member’s name, salary, hire date, and end date.
Appropriate constructors, getters, setters, and other methods you think are necessary.
Overridden toString() and equals(..) methods.
Javadoc for all non-overriding methods and constructors.
*/

package staff;

/**
 * A staff member
 * @author Robert Clifton-Everest
 *
 */
public class StaffMember {
    
    // Attributes to store the staff member’s name, salary, hire date, and end date
    private String name;
    private double salary;
    private String hireDate;
    private String endDate;

    // Constructor
    /**
     * 
     * @param name
     * @param salary
     * @param hireDate
     * @param endDate
     */
    public StaffMember(String name, double salary, String hireDate, String endDate) {
        this.name = name;
        this.salary = salary;
        this.hireDate = hireDate;
        this.endDate = endDate;
    }

    // Getters
    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * @return hireDate
     */
    public String getHireDate() {
        return hireDate;
    }

    /**
     * @return endDate
     */
    public String getEndDate() {
        return endDate;
    }
    
    // Setters
    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @param salary
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * @param hireDate
     */
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }
    
    /**
     * @param endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Override toString() method
     */
    @Override
    public String toString() {
        return "Name: " + name + "\nSalary: " + salary + "\nHire Date: " + hireDate + "\nEnd Date: " + endDate;
    }

    /**
     * Override equals() method
     */
    @Override
    public boolean equals(Object object) {

        if (object instanceof StaffMember == false) return false;

        StaffMember comparison = (StaffMember) object;

        if (this.name == comparison.getName() &&
            this.salary == comparison.getSalary() &&
            this.hireDate == comparison.getHireDate() &&
            this.endDate == comparison.getEndDate()) return true;

        return false;
    }
}
