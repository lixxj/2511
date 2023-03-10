/*
Create a sub-class of StaffMember called Lecturer in the same package with the following requirements:

An attribute to store the school (e.g. CSE) that the lecturer belongs to
An attribute that stores the academic status of the lecturer 
(e.g A for an Associate Lecturer, B  for a Lecturer, and C for a Senior Lecturer)
Appropriate getters and setters.
An overriding toString() method that includes the school name and academic level.
An overriding equals(...) method.
Javadoc for all non-overriding methods and constructors.
*/

package staff;

public class Lecturer extends StaffMember {
    private String school;
    private String academicStatus;

    /**
     * @param name
     * @param salary
     * @param hireDate
     * @param endDate
     * @param school
     * @param academicStatus
     */
    public Lecturer(String name, double salary, String hireDate, String endDate, String school, String academicStatus) {
        super(name, salary, hireDate, endDate);
        this.school = school;
        this.academicStatus = academicStatus;
    }

    // Getters
    /**
     * @return school
     */
    public String getSchool() {
        return school;
    }

    /**
     * @return academicStatus
     */
    public String getAcademicStatus() {
        return academicStatus;
    }

    // Setters
    /**
     * @param school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * @param academicStatus
     */
    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    /**
     * Override toString() method
     */
    @Override
    public String toString() {
        return super.toString() + "\nSchool: " + school + "\nAcademic Status: " + academicStatus;
    }

    /**
     * Override equals() method
     */
    @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) return false;

        if (object instanceof Lecturer == false) return false;

        Lecturer comparison = (Lecturer) object;

        if (this.school == comparison.getSchool() &&
            this.academicStatus == comparison.getAcademicStatus()) return true;

        return false;
    }
}
