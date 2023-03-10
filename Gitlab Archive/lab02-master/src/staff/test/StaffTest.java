/*
Create a class StaffTest in the package staff.test to test the above classes. It should contain:

A method printStaffDetails(...) that takes a StaffMember as a parameter and invokes the toString() 
method on it to print the details of the staff.

A main(...) method that:
Creates a StaffMember with a name and salary of your choosing.
Creates an instance of Lecturer with a name, salary, school and academic status of your choosing.
Calls printStaffDetails(...) twice with each of the above as arguments.
Tests the equals(..) method of the two classes.
*/

package staff.test;

import staff.Lecturer;
import staff.StaffMember;

public class StaffTest {
    
    // Takes a StaffMember as a parameter and invokes the toString() method on it to print the details of the staff
    public static void printStaffDetails(StaffMember staffMember) {
        System.out.println(staffMember.toString());
    }

    public static void main (String[] args) {
        // Creates a StaffMember with a name and salary of your choosing
        StaffMember Staff1 = new StaffMember("AA", 10000, "01/01/1111", "02/02/2222");
        
        //Creates an instance of Lecturer with a name, salary, school and academic status of your choosing
        Lecturer Lecturer1 = new Lecturer("BB", 20000, "03/03/3333", "04/04/4444", "CSE", "A");

        // Calls printStaffDetails(...) twice with each of the above as arguments
        System.out.println("##### PRINTING STAFF1 DETAILS #####");
        printStaffDetails(Staff1);
        System.out.println("##### PRINTING LECTURER1 DETAILS #####");
        printStaffDetails(Lecturer1);

        // Tests the equals(..) method of the two classes
        System.out.println("##### TESTING equals(..) METHOD #####");
        boolean result = true;
        StaffMember Staff1Copy = new StaffMember("AA", 10000, "01/01/1111", "02/02/2222");
        Lecturer Lecturer1Copy = new Lecturer("BB", 20000, "03/03/3333", "04/04/4444", "CSE", "A");

        if (Staff1.equals(Lecturer1)) result = false;
        if (Lecturer1.equals(Staff1)) result = false;
        if (!Staff1.equals(Staff1)) result = false;
        if (!Staff1.equals(Staff1Copy)) result = false;
        if (!Staff1Copy.equals(Staff1)) result = false;
        if (!Lecturer1.equals(Lecturer1Copy)) result = false;
        if (Staff1.equals(null)) result = false;
        if (Staff1Copy.equals(null)) result = false;
        if (Lecturer1.equals(null)) result = false;
        if (Lecturer1Copy.equals(null)) result = false;

        if (result) {
            System.out.println("##### equals(..) TESTS PASSED #####");
        }
        else {
            System.out.println("##### equals(..) TESTS FAILED #####");
        }
    }
}