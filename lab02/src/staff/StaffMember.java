package staff;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * A staff member
 * @author Robert Clifton-Everest
 *
 */
public class StaffMember {
    private String name = "";
    private int salary;
    private LocalDate hireDate;
    private LocalDate endDate;
    
    /**
     * Constructor for Staff Member Object
     * 
     * @param name  The name of the staff member
     * @param salary    The salary of the staff member
     * @param hireDate  The first date of employment for the staff member
     * @param endDate   The last date of employment for the staff member
     */
    public StaffMember(String name, int salary, LocalDate hireDate, LocalDate endDate) {
        setName(name);
        setSalary(salary);
        setHireDate(hireDate);
        setEndDate(endDate);
    }

    /**
     * Gets the name of the staff member
     * @return  The name of the staff member
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the staff member
     * @param name  The name of the staff member
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the salary of the staff member
     * @return  The salary of the staff member
     */
    public int getSalary() {
        return this.salary;
    }

    /**
     * Sets the salary of the staff member
     * @param salary    The salary of the staff member
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * Gets the employee's first date of employment
     * @return The employee's first date of employment as a String object
    */
    public String getHireDate() {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(hireDate);
    }

    /**
     * Sets the employee's first date of employment
     * @param hireDate The employee's first date of employment as a Date object
    */
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    /**
     * Gets the employee's last date of employment
     * @return The employee's last date of employment as a Date object
    */
    public String getEndDate() {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(endDate);
    }

    /**
     * Sets the employee's last date of employment
     * @param endDate The employee's last date of employment as a Date object
    */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        StaffMember other = (StaffMember) obj;
        return this.name.equals(other.name) && this.salary == other.salary && 
            getHireDate().equals(other.getHireDate()) && getEndDate().equals(other.getEndDate());
    }

    @Override
    public String toString() {
        String str = "NAME:" + getName() + "\t SALARY:AUD$" + getSalary() + "\t START_DATE:" + getHireDate() + "\t END_DATE:" + getEndDate(); 
		return str;
    }
}
