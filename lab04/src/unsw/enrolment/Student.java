package unsw.enrolment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Student {

    private String zid;
    private ArrayList<Enrolment> enrolments = new ArrayList<Enrolment>();
    private String name;
    private int program;
    private String[] streams;

	public Student(String zid, String name, int program, String[] streams) {
        this.zid = zid;
        this.name = name;
        this.program = program;
        this.streams = streams;
    }

    public String getZid() {
        return zid;
    }

    public String getName() {
        return name;
    }

    public int getProgram() {
        return program;
    }

    public String[] getStreams() {
        return streams;
    }

    public int getNumStreams() {
        return streams.length;
    }

    public boolean isEnrolled(CourseOffering offering) {
        return enrolments.stream().anyMatch(e -> e.getOffering().equals(offering));
    }

    public void setGrade(Grade grade) {
        enrolments.forEach(e -> {
            if (e.getOffering().equals(grade.getOffering())) e.setGrade(grade);
        });
    }

    public void addEnrolment(Enrolment enrolment) {
        enrolments.add(enrolment);
    }

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public boolean completedCourse(Course course) {
        Optional<Enrolment> enrolledInCourse = enrolments.stream().filter(e -> e.getCourse().equals(course)).findFirst();
        // If enrolled, check if passed
        if (enrolledInCourse.isPresent()) return enrolledInCourse.get().hasPassedCourse();
        // Not enrolled
        return false;
    }

}
