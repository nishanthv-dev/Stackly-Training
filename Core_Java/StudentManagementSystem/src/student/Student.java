package student;

public class Student {

	private int id;
	private String name;
	private String email;
	private double marks;
	private int attendance;
	private StudentStatus status;
	private Course course;

	public Student(int id, String name, String email, double marks, int attendance, StudentStatus status, Course course) {
	    this.id = id;
	    this.name = name;
	    this.email = email;
	    this.marks = marks;
	    this.attendance = attendance;
	    this.status = status;
	    this.course = course;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}