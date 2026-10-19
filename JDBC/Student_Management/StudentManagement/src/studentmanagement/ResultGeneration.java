
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultGeneration {

    public static void generateResults() {

        String query = "SELECT s.student_id, s.student_name, "
                + "c.course_name, "
                + "SUM(m.marks_obtained) AS obtained, "
                + "SUM(m.total_marks) AS total "
                + "FROM enrollments e "
                + "JOIN students s ON e.student_id = s.student_id "
                + "JOIN courses c ON e.course_id = c.course_id "
                + "LEFT JOIN marks m ON e.enrollment_id = m.enrollment_id "
                + "GROUP BY s.student_id, s.student_name, "
                + "e.enrollment_id, c.course_name "
                + "ORDER BY s.student_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== STUDENT RESULTS =====");

            boolean found = false;

            while (rs.next()) {
                found = true;

                double obtained = rs.getDouble("obtained");
                boolean noMarks = rs.wasNull();

                double total = rs.getDouble("total");

                System.out.println("Student ID: "
                        + rs.getInt("student_id"));
                System.out.println("Student Name: "
                        + rs.getString("student_name"));
                System.out.println("Course: "
                        + rs.getString("course_name"));

                if (noMarks || total == 0) {
                    System.out.println("Marks: No Marks Entered");
                    System.out.println("Percentage: N/A");
                    System.out.println("Result: Pending");
                } else {
                    double percentage = (obtained / total) * 100;
                    String result = percentage >= 40 ? "PASS" : "FAIL";

                    System.out.println("Marks: "
                            + obtained + " / " + total);
                    System.out.printf("Percentage: %.2f%%%n",
                            percentage);
                    System.out.println("Result: " + result);
                }

                System.out.println("---------------------------");
            }

            if (!found) {
                System.out.println("No enrolled students found.");
            }

        } catch (SQLException e) {
            System.out.println("Error generating results: "
                    + e.getMessage());
        }
    }
}
