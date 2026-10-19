package employeeleavemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LeaveHistory {

    public static void viewHistory(Scanner sc) {

        System.out.println("\n===== STACKLY LEAVE HISTORY =====");
        System.out.print("Enter Employee Code: ");
        String employeeCode = sc.nextLine().trim();

        if (employeeCode.isEmpty()) {
            System.out.println("Employee code cannot be empty!");
            return;
        }

        String sql = """
                SELECT e.employee_code,
                       e.employee_name,
                       e.department,
                       lr.request_id,
                       lr.leave_type,
                       lr.start_date,
                       lr.end_date,
                       lr.total_days,
                       lr.reason,
                       lr.status
                FROM employees e
                JOIN leave_requests lr
                ON e.employee_id = lr.employee_id
                WHERE e.employee_code = ?
                ORDER BY lr.request_id DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, employeeCode);

            try (ResultSet rs = ps.executeQuery()) {

                boolean found = false;

                while (rs.next()) {

                    if (!found) {
                        found = true;

                        System.out.println("\n----------------------------------------");
                        System.out.println("         STACKLY LEAVE HISTORY");
                        System.out.println("----------------------------------------");
                        System.out.println("Employee Code : "
                                + rs.getString("employee_code"));
                        System.out.println("Employee Name : "
                                + rs.getString("employee_name"));
                        System.out.println("Department    : "
                                + rs.getString("department"));
                    }

                    System.out.println("\n----------------------------------------");
                    System.out.println("Request ID : "
                            + rs.getInt("request_id"));
                    System.out.println("Leave Type : "
                            + rs.getString("leave_type"));
                    System.out.println("Start Date : "
                            + rs.getDate("start_date"));
                    System.out.println("End Date   : "
                            + rs.getDate("end_date"));
                    System.out.println("Total Days : "
                            + rs.getInt("total_days"));
                    System.out.println("Reason     : "
                            + rs.getString("reason"));
                    System.out.println("Status     : "
                            + rs.getString("status"));
                }

                if (!found) {
                    System.out.println(
                            "No leave history found for this employee, "
                            + "or the employee code is invalid.");
                } else {
                    System.out.println("----------------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error viewing leave history: "
                    + e.getMessage());
        }
    }
}
