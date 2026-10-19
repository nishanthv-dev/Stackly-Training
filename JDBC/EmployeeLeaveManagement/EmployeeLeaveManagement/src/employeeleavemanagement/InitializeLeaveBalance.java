
package employeeleavemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InitializeLeaveBalance {

    public static void main(String[] args) {

        String query = """
                INSERT INTO leave_balance
                (employee_id, total_leaves, used_leaves, remaining_leaves)
                SELECT e.employee_id, 12, 0, 12
                FROM employees e
                LEFT JOIN leave_balance lb
                    ON e.employee_id = lb.employee_id
                WHERE lb.employee_id IS NULL
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            int rows = ps.executeUpdate();

            System.out.println(
                    rows + " employee leave balance record(s) initialized.");

        } catch (SQLException e) {
            System.out.println("Failed to initialize leave balances.");
            e.printStackTrace();
        }
    }
}
