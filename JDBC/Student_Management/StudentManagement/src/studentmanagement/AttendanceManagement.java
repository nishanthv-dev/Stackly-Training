
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class AttendanceManagement {

    // Create Attendance Table
    public static void createAttendanceTable() {

        String query = "CREATE TABLE IF NOT EXISTS attendance ("
                + "attendance_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "enrollment_id INT NOT NULL, "
                + "attendance_date DATE NOT NULL, "
                + "status VARCHAR(10) NOT NULL, "
                + "FOREIGN KEY (enrollment_id) "
                + "REFERENCES enrollments(enrollment_id), "
                + "UNIQUE (enrollment_id, attendance_date), "
                + "CHECK (status IN ('Present', 'Absent'))"
                + ")";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(query);
            System.out.println("Attendance Table Created Successfully!");

        } catch (SQLException e) {
            System.out.println("Error creating attendance table: "
                    + e.getMessage());
        }
    }

    // Display Enrolled Students
    public static void displayEnrolledStudents() {

        String query = "SELECT e.enrollment_id, s.student_name, "
                + "c.course_name "
                + "FROM enrollments e "
                + "JOIN students s ON e.student_id = s.student_id "
                + "JOIN courses c ON e.course_id = c.course_id "
                + "ORDER BY e.enrollment_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\n----- ENROLLED STUDENTS -----");
            System.out.printf("%-15s %-20s %-25s%n",
                    "Enrollment ID", "Student Name", "Course Name");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.printf("%-15d %-20s %-25s%n",
                        rs.getInt("enrollment_id"),
                        rs.getString("student_name"),
                        rs.getString("course_name"));
            }

            if (!found) {
                System.out.println("No enrolled students found.");
            }

        } catch (SQLException e) {
            System.out.println("Error displaying enrolled students: "
                    + e.getMessage());
        }
    }

    // Mark Attendance
    public static void markAttendance(int enrollmentId, String status) {

        if (!status.equalsIgnoreCase("Present")
                && !status.equalsIgnoreCase("Absent")) {
            System.out.println("Invalid status! Enter Present or Absent.");
            return;
        }

        String checkQuery =
                "SELECT enrollment_id FROM enrollments WHERE enrollment_id = ?";

        String insertQuery = "INSERT INTO attendance "
                + "(enrollment_id, attendance_date, status) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            // Check whether Enrollment ID exists
            try (PreparedStatement check =
                    con.prepareStatement(checkQuery)) {

                check.setInt(1, enrollmentId);

                try (ResultSet rs = check.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println(
                                "Invalid Enrollment ID! Please select an ID from the list.");
                        return;
                    }
                }
            }

            // Insert Attendance
            try (PreparedStatement ps =
                    con.prepareStatement(insertQuery)) {

                ps.setInt(1, enrollmentId);
                ps.setDate(2,
                        java.sql.Date.valueOf(LocalDate.now()));
                ps.setString(3,
                        status.substring(0, 1).toUpperCase()
                                + status.substring(1).toLowerCase());

                ps.executeUpdate();

                System.out.println("Attendance Marked Successfully!");
            }

        } catch (SQLException e) {

            if (e.getErrorCode() == 1062) {
                System.out.println(
                        "Attendance already marked for this enrollment today!");
            } else {
                System.out.println("Unable to mark attendance: "
                        + e.getMessage());
            }
        }
    }

    // View Attendance
    public static void viewAttendance() {

        String query = "SELECT a.enrollment_id, s.student_name, "
                + "c.course_name, a.attendance_date, a.status "
                + "FROM attendance a "
                + "JOIN enrollments e ON a.enrollment_id = e.enrollment_id "
                + "JOIN students s ON e.student_id = s.student_id "
                + "JOIN courses c ON e.course_id = c.course_id "
                + "ORDER BY a.attendance_date DESC, a.enrollment_id";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("\n----- ATTENDANCE RECORDS -----");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Enrollment ID: "
                        + rs.getInt("enrollment_id"));
                System.out.println("Student: "
                        + rs.getString("student_name"));
                System.out.println("Course: "
                        + rs.getString("course_name"));
                System.out.println("Date: "
                        + rs.getDate("attendance_date"));
                System.out.println("Status: "
                        + rs.getString("status"));
                System.out.println("------------------------------");
            }

            if (!found) {
                System.out.println("No attendance records found.");
            }

        } catch (SQLException e) {
            System.out.println("Error viewing attendance: "
                    + e.getMessage());
        }
    }
}
