package staff;

import java.time.LocalDate;

public class Lecturer extends StaffMember {

    private String school;
    private char status;

    /**
     * Constructor for Lecturer
     * 
     * @param name  The name of the lecturer
     * @param salary    The salary of the lecturer
     * @param hireDate  The first date of employment for the lecturer
     * @param endDate   The last date of employment for the lecturer
     * @param school    The school/faculty the lecturer works in
     * @param status    The academic status of the lecturer (e.g A for an Associate Lecturer, B  for a Lecturer, and C for a Senior Lecturer)
     */
    public Lecturer(String name, int salary, LocalDate hireDate, LocalDate endDate, String school, char status) {
        super(name, salary, hireDate, endDate);
        this.school = school;
        this.status = status;
    }

    /**
     * Sets the value for the school the lecturer works in
     * @param school The school/faculty the lecturer works in
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Sets the academic status of the lecturer
     * @param status The academic status of the lecturer
     */
    public void setStatus(char academicStatus) {
        this.status = academicStatus;
    }

    /**
     * Gets the school the lecturer works in
     * @return  The school that the lecturer works in
     */
    public String getSchool() {
        return this.school;
    }

    /**
     * Gets the academic status of the lecturer
     * @return  The academic status of the lecturer
     */
    public char getStatus() {
        return this.status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;

        Lecturer other = (Lecturer) obj;
        return school.equals(other.getSchool()) && (this.status == other.getStatus());
    }

    @Override
    public String toString() {
        String str = super.toString() + 
                     "\t SCHOOL:" + getSchool() + "\t STATUS:" + getStatus(); 
		return str;
    }
}
