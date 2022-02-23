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
        if (checkValidEnrolment(student)) {                         // LSP satisfied
            Enrolment enrolment = new Enrolment(this, student);     // LSP satisfied
            enrolments.add(enrolment);                              // LSP satisfied
            student.addEnrolment(enrolment);                        // LSP satisfied
            return enrolment;
        } else {
            throw new InvalidEnrolmentException("student has not satisfied the prerequisites");
        }
    }

    private boolean checkValidEnrolment(Student student) {
        return !getCoursePrereqs().stream().anyMatch(p -> !student.completedCourse(p));
    }

    public List<Student> studentsEnrolledInCourse() {
        List<Student> students = enrolments.stream().map(Enrolment::getStudent).collect(Collectors.toList());

        students.sort(Comparator.comparing(Student::getProgram).thenComparing(Student::getNumStreams)
                        .thenComparing(Student::getName).thenComparing(Student::getZid));

        // Comparator<Student> myCmp = new Comparator<Student>() {
        //     @Override
        //     public int compare(Student s1, Student s2) {
        //         if (s1.getProgram() != s2.getProgram()) {
        //             return s1.getProgram() - s2.getProgram();
        //         } else if (s1.getStreams().length != s2.getStreams().length) {
        //             return s1.getStreams().length - s2.getStreams().length;
        //         } else if (s1.getName() != s2.getName()) {
        //             return s1.getName().compareTo(s2.getName());
        //         }
        //         return s1.getZid().compareTo(s2.getZid());
        //     }
        // };

        // Collections.sort(students, myCmp);


        return students;
    }

}