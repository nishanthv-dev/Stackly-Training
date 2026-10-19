
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



public class CourseManagement {

    public static void createCourseTable() {

        String query = "CREATE TABLE IF NOT EXISTS courses ("
                + "course_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "course_name VARCHAR(100) NOT NULL, "
                + "duration VARCHAR(50), "
                + "fee DECIMAL(10,2))";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(query);
            System.out.println("Courses Table Created Successfully!");

        } catch (SQLException e) {
            System.out.println("Error creating courses table: "
                    + e.getMessage());
        }
    }

    public static void addCourse(String name, String duration,
                                 double fee) {

        String query = "INSERT INTO courses "
                + "(course_name, duration, fee) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, duration);
            ps.setDouble(3, fee);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Course Added Successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
        
        
    }

public static void viewCourses() {

    String query = "SELECT * FROM courses";

    try (Connection con = DBConnection.getConnection();
         Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery(query)) {

        System.out.println("\n----- Available Courses -----");

        while (rs.next()) {
            System.out.println("Course ID: " + rs.getInt("course_id"));
            System.out.println("Course Name: " + rs.getString("course_name"));
            System.out.println("Duration: " + rs.getString("duration"));
            System.out.println("Fee: " + rs.getDouble("fee"));
            System.out.println("-----------------------------");
        }

    } catch (SQLException e) {
        System.out.println("Error viewing courses: " + e.getMessage());
    }
}



public static void addCourse() {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter course name: ");
    String name = sc.nextLine();

    System.out.print("Enter course duration: ");
    String duration = sc.nextLine();

    System.out.print("Enter course fee: ");
    double fee = Double.parseDouble(sc.nextLine());

    addCourse(name, duration, fee);
}


public static void createEnrollmentTable() {

    String query = "CREATE TABLE IF NOT EXISTS enrollments ("
            + "enrollment_id INT PRIMARY KEY AUTO_INCREMENT, "
            + "student_id INT NOT NULL, "
            + "course_id INT NOT NULL, "
            + "enrollment_date DATE NOT NULL, "
            + "FOREIGN KEY (student_id) REFERENCES students(student_id), "
            + "FOREIGN KEY (course_id) REFERENCES courses(course_id), "
            + "UNIQUE (student_id, course_id))";

    try (Connection con = DBConnection.getConnection();
         Statement st = con.createStatement()) {

        st.executeUpdate(query);
        System.out.println("Enrollments Table Created Successfully!");

    } catch (SQLException e) {
        System.out.println("Error creating enrollments table: "
                + e.getMessage());
    }
}


public static void enrollStudent(int studentId, int courseId) {

    String query = "INSERT INTO enrollments "
            + "(student_id, course_id, enrollment_date) "
            + "VALUES (?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ps.setDate(3, java.sql.Date.valueOf(
                java.time.LocalDate.now()));

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Student Enrolled Successfully!");
        }

    } catch (SQLException e) {
        System.out.println("Enrollment failed: " + e.getMessage());
    }
}




public static void viewEnrollments() {

    String query = "SELECT e.enrollment_id, "
            + "s.student_name, c.course_name, e.enrollment_date "
            + "FROM enrollments e "
            + "JOIN students s ON e.student_id = s.student_id "
            + "JOIN courses c ON e.course_id = c.course_id";

    try (Connection con = DBConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {

        System.out.println("\n----- Student Course Enrollments -----");

        while (rs.next()) {
            System.out.println("Enrollment ID: "
                    + rs.getInt("enrollment_id"));
            System.out.println("Student Name: "
                    + rs.getString("student_name"));
            System.out.println("Course Name: "
                    + rs.getString("course_name"));
            System.out.println("Enrollment Date: "
                    + rs.getDate("enrollment_date"));
            System.out.println("--------------------------------------");
        }

    } catch (SQLException e) {
        System.out.println("Error viewing enrollments: "
                + e.getMessage());
    }
}





public static void enrollStudentTransaction(int studentId, int courseId) {

    String query = "INSERT INTO enrollments "
            + "(student_id, course_id, enrollment_date) "
            + "VALUES (?, ?, ?)";

    String detailsQuery = "SELECT s.student_name, c.course_name "
            + "FROM students s "
            + "JOIN courses c ON c.course_id = ? "
            + "WHERE s.student_id = ?";

    try (Connection con = DBConnection.getConnection()) {

        con.setAutoCommit(false);

        try (PreparedStatement ps = con.prepareStatement(
                query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setDate(3, java.sql.Date.valueOf(
                    java.time.LocalDate.now()));

            int rows = ps.executeUpdate();

            if (rows > 0) {

                int enrollmentId;

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        enrollmentId = keys.getInt(1);
                    } else {
                        throw new SQLException(
                                "Enrollment ID was not generated.");
                    }
                }

                String studentName;
                String courseName;

                try (PreparedStatement details =
                        con.prepareStatement(detailsQuery)) {

                    details.setInt(1, courseId);
                    details.setInt(2, studentId);

                    try (ResultSet rs = details.executeQuery()) {
                        if (rs.next()) {
                            studentName = rs.getString("student_name");
                            courseName = rs.getString("course_name");
                        } else {
                            throw new SQLException(
                                    "Student or course details not found.");
                        }
                    }
                }

                con.commit();

                System.out.println("\nEnrollment Successful!");
                System.out.println("Transaction Committed.");
                System.out.println("Enrollment ID: " + enrollmentId);
                System.out.println("Student Name: " + studentName);
                System.out.println("Course Name: " + courseName);

            } else {
                con.rollback();
                System.out.println("Enrollment Failed. No records inserted.");
            }

        } catch (SQLException e) {
            con.rollback();
            System.out.println(
                    "Enrollment Failed! Transaction Rolled Back.");
            System.out.println("Reason: " + e.getMessage());
        }

    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
}





}
