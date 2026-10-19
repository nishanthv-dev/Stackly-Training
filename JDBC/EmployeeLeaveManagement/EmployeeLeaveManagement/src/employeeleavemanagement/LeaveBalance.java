package employeeleavemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LeaveBalance {

    public static void viewBalance(Scanner sc) {

        System.out.println("\n===== STACKLY LEAVE BALANCE =====");
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
                       lb.total_leaves,
                       lb.used_leaves,
                       lb.remaining_leaves
                FROM employees e
                JOIN leave_balance lb
                ON e.employee_id = lb.employee_id
                WHERE e.employee_code = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, employeeCode);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println("\n----------------------------------------");
                    System.out.println("       STACKLY LEAVE BALANCE");
                    System.out.println("----------------------------------------");

                    System.out.println("Employee Code    : "
                            + rs.getString("employee_code"));
                    System.out.println("Employee Name    : "
                            + rs.getString("employee_name"));
                    System.out.println("Department       : "
                            + rs.getString("department"));
                    System.out.println("Total Leaves     : "
                            + rs.getInt("total_leaves"));
                    System.out.println("Used Leaves      : "
                            + rs.getInt("used_leaves"));
                    System.out.println("Remaining Leaves : "
                            + rs.getInt("remaining_leaves"));

                    System.out.println("----------------------------------------");
                } else {
                    System.out.println(
                            "Employee not found or leave balance unavailable.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error viewing leave balance: "
                    + e.getMessage());
        }
    }
}