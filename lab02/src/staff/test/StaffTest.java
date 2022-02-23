package staff.test;

import java.time.LocalDate;

import staff.Lecturer;
import staff.StaffMember;

public class StaffTest {
    public void printStaffDetails(StaffMember sMember) {
        System.out.println(sMember.toString());
    }

    public static void main(String[] args) {

        LocalDate date1 = LocalDate.of(2021, 3, 24);
        LocalDate date2 = LocalDate.of(2021, 9, 24);

        StaffMember s1 = new StaffMember("Jinghao", 100_000, date1, date2);
        StaffMember s2 = new StaffMember("Ashesh", 120_000, date1, date2);

        Lecturer l1 = new Lecturer("Ashesh", 120_000, date1, date2, "CSE", 'C');
        Lecturer l2 = new Lecturer("Ashesh", 120_000, date1, date2, "CSE", 'C');

        StaffTest test = new StaffTest();
        System.out.println("=== Printing out Staff Members ===>");
        test.printStaffDetails(s1);
        test.printStaffDetails(s2);
        test.printStaffDetails(l1);
        test.printStaffDetails(l2);

        System.out.println("\n============\n...Testing...\n============");
        // Check properties of each staff member and lecturer object
        if (!s1.equals(s2)) System.out.println("Test 1 passed!"); else System.out.println("Test 1 failed!");
        if(!s1.equals(l1)) System.out.println("Test 2 passed!"); else System.out.println("Test 2 failed!");
        if(!s2.equals(l1)) System.out.println("Test 3 passed!"); else System.out.println("Test 3 failed!");
        
        if(!l1.equals(s2)) System.out.println("Test 4 passed!"); else System.out.println("Test 4 failed!");
        if(s1.equals(s1)) System.out.println("Test 5 passed!"); else System.out.println("Test 5 failed!");
        if(l1.equals(l1)) System.out.println("Test 6 passed!"); else System.out.println("Test 6 failed!");
        if(l1.equals(l2)) System.out.println("Test 7 passed!"); else System.out.println("Test 7 failed!");
        if(!l1.equals(null)) System.out.println("Test 8 passed!"); else System.out.println("Test 8 failed!");
    }
}
