package student;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        StudentService studentService = new StudentService();
        StudentFileService fileService = new StudentFileService();

        int choice;

        do {
            System.out.println("\n===== Student Management System =====");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Search Student by Name");
            System.out.println("5. Update Marks");
            System.out.println("6. Update Attendance");
            System.out.println("7. Calculate Grade");
            System.out.println("8. Remove Student");
            System.out.println("9. Display Topper");
            System.out.println("10. Save Students");
            System.out.println("11. Load Students");
            System.out.println("12. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            
            switch (choice) {

            case 1:

                System.out.print("Enter Student ID: ");
                int id = sc.nextInt();

                System.out.print("Enter Student Name: ");
                String name = sc.next();

                System.out.print("Enter Email: ");
                String email = sc.next();

                System.out.print("Enter Marks: ");
                double marks = sc.nextDouble();

                System.out.print("Enter Attendance: ");
                int attendance = sc.nextInt();

                System.out.println("1. ACTIVE");
                System.out.println("2. INACTIVE");
                System.out.println("3. COMPLETED");

                System.out.print("Enter Status: ");
                int statusChoice = sc.nextInt();

                StudentStatus status;

                if (statusChoice == 1) {
                    status = StudentStatus.ACTIVE;
                } else if (statusChoice == 2) {
                    status = StudentStatus.INACTIVE;
                } else if (statusChoice == 3) {
                    status = StudentStatus.COMPLETED;
                } else {
                    System.out.println("Invalid status");
                    break;
                }

                System.out.println("1. Java");
                System.out.println("2. Python");
                System.out.println("3. Full Stack Development");

                System.out.print("Enter Course: ");
                int courseChoice = sc.nextInt();

                Course course;

                if (courseChoice == 1) {
                    course = new Course(101, "Java");
                } else if (courseChoice == 2) {
                    course = new Course(102, "Python");
                } else if (courseChoice == 3) {
                    course = new Course(103, "Full Stack Development");
                } else {
                    System.out.println("Invalid course");
                    break;
                }

                try {

                    if (marks < 0 || marks > 100) {
                        throw new StudentException("Marks must be between 0 and 100");
                    }

                    if (attendance < 0 || attendance > 100) {
                        throw new StudentException("Attendance must be between 0 and 100");
                    }

                    Student student = new Student(id, name, email, marks, attendance, status, course);

                    studentService.addStudent(student);

                } catch (StudentException e) {

                    System.out.println(e.getMessage());

                }

                break;

            case 2:
                studentService.displayAllStudents();
                break;

            case 3:
                System.out.print("Enter Student ID: ");
                int searchId = sc.nextInt();

                Student student = studentService.searchById(searchId);

                if (student != null) {
                    System.out.println("ID: " + student.getId());
                    System.out.println("Name: " + student.getName());
                    System.out.println("Email: " + student.getEmail());
                    System.out.println("Marks: " + student.getMarks());
                    System.out.println("Attendance: " + student.getAttendance());
                    System.out.println("Status: " + student.getStatus());
                    System.out.println("Course ID: " + student.getCourse().getCourseId());
                    System.out.println("Course Name: " + student.getCourse().getCourseName());
                } else {
                    System.out.println("Student not found");
                }

                break;

            case 4:
                System.out.print("Enter Student Name: ");
                String searchName = sc.next();

                Student studentByName = studentService.searchByName(searchName);

                if (studentByName != null) {
                    System.out.println("ID: " + studentByName.getId());
                    System.out.println("Name: " + studentByName.getName());
                    System.out.println("Email: " + studentByName.getEmail());
                    System.out.println("Marks: " + studentByName.getMarks());
                    System.out.println("Attendance: " + studentByName.getAttendance());
                    System.out.println("Status: " + studentByName.getStatus());
                    System.out.println("Course ID: " + studentByName.getCourse().getCourseId());
                    System.out.println("Course Name: " + studentByName.getCourse().getCourseName());
                } else {
                    System.out.println("Student not found");
                }

                break;

            case 5:
                System.out.print("Enter Student ID: ");
                int marksId = sc.nextInt();

                System.out.print("Enter New Marks: ");
                double newMarks = sc.nextDouble();

                try {
                    studentService.updateMarks(marksId, newMarks);
                } catch (StudentException e) {
                    System.out.println(e.getMessage());
                }

                break;
                
            case 6:
                System.out.print("Enter Student ID: ");
                int attendanceId = sc.nextInt();

                System.out.print("Enter New Attendance: ");
                int newAttendance = sc.nextInt();

                try {
                    studentService.updateAttendance(attendanceId, newAttendance);
                } catch (StudentException e) {
                    System.out.println(e.getMessage());
                }

                break;

            case 7:
                System.out.print("Enter Student ID: ");
                int gradeId = sc.nextInt();

                String grade = studentService.calculateGrade(gradeId);

                System.out.println("Grade: " + grade);
                break;

            case 8:
                System.out.print("Enter Student ID: ");
                int removeId = sc.nextInt();

                studentService.removeStudent(removeId);
                break;

            case 9:
                Student topper = studentService.displayTopper();

                if (topper != null) {
                    System.out.println("Topper ID: " + topper.getId());
                    System.out.println("Topper Name: " + topper.getName());
                    System.out.println("Topper Marks: " + topper.getMarks());
                } else {
                    System.out.println("No students available");
                }

                break;

            case 10:
                fileService.saveStudents(studentService);
                break;

            case 11:
                fileService.loadStudents(studentService);
                break;

            case 12:
                System.out.println("Thank you");
                break;

            default:
                System.out.println("Invalid choice");
            

            }

        } while (choice != 12);

        sc.close();
    }
    
}