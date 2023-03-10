package unsw.enrolment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import unsw.enrolment.exceptions.InvalidEnrolmentException;

public class CourseOffering {

    private Course course;
    private String term;
    private List<Enrolment> enrolments = new ArrayList<Enrolment>();

    public CourseOffering(Course course, String term) {
        this.course = course;
        this.term = term;
    }

    public Course getCourse() {
        return course;
    }

    public String getCourseCode() {
        return course.getCourseCode();
    }

    public List<Course> getCoursePrereqs() {
        return course.getPrereqs();
    }

    public String getTerm() {
        return term;
    }

    public Enrolment addEnrolment(Student student) throws InvalidEnrolmentException {
        if (checkValidEnrolment(student)) {
            Enrolment enrolment = new Enrolment(this, student);
            enrolments.add(enrolment);
            student.addEnrolment(enrolment);
            return enrolment;
        } else {
            throw new InvalidEnrolmentException("student has not satisfied the prerequisites");
        }
    }

    private boolean checkValidEnrolment(Student student) {
        List<Course> prereqs = getCoursePrereqs();

        for (Course prereq : prereqs) {
            List<Enrolment> studentEnrolments = student.getEnrolments();
            boolean valid = false;

            for (Enrolment enrolment : studentEnrolments) {
                if (enrolment.getCourse().equals(prereq) && enrolment.getGrade() != null) {
                    if (enrolment.hasPassedCourse()) valid = true;
                }

                if (!valid) {
                    return false;
                }
            }
        }

        return true;
    }

    public List<Student> studentsEnrolledInCourse() {
        List<Student> students = enrolments.stream().map(Enrolment::getStudent).collect(Collectors.toList());
        
        // Firstly use an anonymous class to implement the comparator.
        /*Collections.sort(students, new Comparator<Student>() 
            {
                public int compare(Student s1, Student s2) {
                    int i = s1.getProgram() - s2.getProgram();
                    if (i != 0) return i;

                    i = s1.getNumStreams() - s2.getNumStreams();
                    if (i != 0) return i;

                    i = s1.getName().compareTo(s2.getName());
                    if (i != 0) return i;
                    
                    return s1.getZid().compareTo(s2.getZid());
                }
            }    
        );*/
        
        // Once you have done this, comment out your anonymous class

        // implement the sorting in one line of code
        students.sort(Comparator.comparing(Student::getProgram)
                                .thenComparing(Student::getNumStreams)
                                .thenComparing(Student::getName)
                                .thenComparing(Student::getZid));
        
        return students;
    }

}