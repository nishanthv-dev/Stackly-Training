
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MarksManagement {

    public static void createMarksTable() {

        String query = "CREATE TABLE IF NOT EXISTS marks ("
                + "mark_id INT PRIMARY KEY AUTO_INCREMENT, "
                + "enrollment_id INT NOT NULL, "
                + "subject VARCHAR(100) NOT NULL, "
                + "marks_obtained DECIMAL(5,2) NOT NULL, "
                + "total_marks DECIMAL(5,2) NOT NULL, "
                + "FOREIGN KEY (enrollment_id) "
                + "REFERENCES enrollments(enrollment_id), "
                + "UNIQUE (enrollment_id, subject), "
                + "CHECK (marks_obtained >= 0), "
                + "CHECK (total_marks > 0), "
                + "CHECK (marks_obtained <= total_marks)"
                + ")";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(query);
            System.out.println("Marks Table Created Successfully!");

        } catch (SQLException e) {
            System.out.println("Error creating marks table: "
                    + e.getMessage());
        }
    }
    
    

public static void enterMarks(int enrollmentId, String subject,
                              double marksObtained, double totalMarks) {

	String query = "INSERT INTO marks "
	        + "(enrollment_id, subject, marks_obtained, total_marks) "
	        + "VALUES (?, ?, ?, ?)";

    if (marksObtained < 0 || totalMarks <= 0
            || marksObtained > totalMarks) {
        System.out.println("Invalid marks! Please check the values.");
        return;
    }

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setInt(1, enrollmentId);
        ps.setString(2, subject);
        ps.setDouble(3, marksObtained);
        ps.setDouble(4, totalMarks);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Marks Entered Successfully!");
        }

    } catch (SQLException e) {
        System.out.println("Error entering marks: " + e.getMessage());
    }
     
}


public static void viewMarks() {

    String query = "SELECT s.student_name, c.course_name, "
            + "m.subject, m.marks_obtained, m.total_marks "
            + "FROM marks m "
            + "JOIN enrollments e ON m.enrollment_id = e.enrollment_id "
            + "JOIN students s ON e.student_id = s.student_id "
            + "JOIN courses c ON e.course_id = c.course_id "
            + "ORDER BY s.student_name, m.subject";

    try (Connection con = DBConnection.getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {

        System.out.println("\n----- Student Marks -----");

        while (rs.next()) {
            System.out.println("Student: "
                    + rs.getString("student_name"));
            System.out.println("Course: "
                    + rs.getString("course_name"));
            System.out.println("Subject: "
                    + rs.getString("subject"));
            System.out.println("Marks: "
                    + rs.getDouble("marks_obtained") + " / "
                    + rs.getDouble("total_marks"));
            System.out.println("-------------------------");
        }

    } catch (SQLException e) {
        System.out.println("Error viewing marks: " + e.getMessage());
    }
}




public static void updateMarks(int enrollmentId, String subject,
                               double marksObtained, double totalMarks) {

    if (marksObtained < 0 || totalMarks <= 0
            || marksObtained > totalMarks) {
        System.out.println("Invalid marks! Please check the values.");
        return;
    }

    String query = "UPDATE marks "
            + "SET marks_obtained = ?, total_marks = ? "
            + "WHERE enrollment_id = ? AND subject = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(query)) {

        ps.setDouble(1, marksObtained);
        ps.setDouble(2, totalMarks);
        ps.setInt(3, enrollmentId);
        ps.setString(4, subject);

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Marks Updated Successfully!");
        } else {
            System.out.println("No matching marks record found!");
        }

    } catch (SQLException e) {
        System.out.println("Error updating marks: " + e.getMessage());
    }
}


}
