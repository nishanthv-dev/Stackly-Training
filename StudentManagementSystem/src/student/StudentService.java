package student;

import java.util.HashMap;
import java.util.Map;

public class StudentService {

    private Map<Integer, Student> students = new HashMap<>();

    public void addStudent(Student student) throws StudentException {

        if (students.containsKey(student.getId())) {
            throw new StudentException("Student ID already exists");
        }

        students.put(student.getId(), student);
        System.out.println("Student added successfully");
    }
    
    public Student searchByName(String name) {

        for (Student student : students.values()) {

            if (student.getName().equalsIgnoreCase(name)) {
                return student;
            }
        }

        return null;
    }
    
    public void updateMarks(int id, double marks) throws StudentException {

        if (marks < 0 || marks > 100) {
            throw new StudentException("Marks must be between 0 and 100");
        }

        Student student = students.get(id);

        if (student != null) {
            student.setMarks(marks);
            System.out.println("Marks updated successfully");
        } else {
            System.out.println("Student not found");
        }
    }

    public void displayAllStudents() {

        for (Student student : students.values()) {
            System.out.println("ID: " + student.getId());
            System.out.println("Name: " + student.getName());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Marks: " + student.getMarks());
            System.out.println("Attendance: " + student.getAttendance());
            System.out.println("Status: " + student.getStatus());
            System.out.println("Course ID: " + student.getCourse().getCourseId());
            System.out.println("Course Name: " + student.getCourse().getCourseName());
            System.out.println("----------------------");
        }
    }
    
    
    public void updateAttendance(int id, int attendance) throws StudentException {

        if (attendance < 0 || attendance > 100) {
            throw new StudentException("Attendance must be between 0 and 100");
        }

        Student student = students.get(id);

        if (student != null) {
            student.setAttendance(attendance);
            System.out.println("Attendance updated successfully");
        } else {
            System.out.println("Student not found");
        }
    }
    
    
    public String calculateGrade(int id) {

        Student student = students.get(id);

        if (student != null) {

            double marks = student.getMarks();

            if (marks >= 90) {
                return "A+";
            } else if (marks >= 80) {
                return "A";
            } else if (marks >= 70) {
                return "B";
            } else if (marks >= 60) {
                return "C";
            } else if (marks >= 50) {
                return "D";
            } else {
                return "F";
            }

        } else {
            return "Student not found";
        }
    }
    
    
    public void removeStudent(int id) {

        if (students.containsKey(id)) {
            students.remove(id);
            System.out.println("Student removed successfully");
        } else {
            System.out.println("Student not found");
        }
    }
    
    
    
    public Student displayTopper() {

        Student topper = null;

        for (Student student : students.values()) {

            if (topper == null || student.getMarks() > topper.getMarks()) {
                topper = student;
            }
        }

        return topper;
    }
    
 
    
    public Student searchById(int id) {

        return students.get(id);
    }
    
    public Map<Integer, Student> getStudents() {
        return students;
    }
}