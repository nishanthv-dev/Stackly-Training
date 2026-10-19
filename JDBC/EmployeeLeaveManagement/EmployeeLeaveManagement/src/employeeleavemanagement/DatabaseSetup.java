
package employeeleavemanagement;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void main(String[] args) {

        String employeesTable = """
                CREATE TABLE IF NOT EXISTS employees (
                    employee_id INT PRIMARY KEY AUTO_INCREMENT,
                    employee_name VARCHAR(100) NOT NULL,
                    department VARCHAR(50) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    joining_date DATE NOT NULL
                )
                """;

        String leaveBalanceTable = """
                CREATE TABLE IF NOT EXISTS leave_balance (
                    balance_id INT PRIMARY KEY AUTO_INCREMENT,
                    employee_id INT NOT NULL UNIQUE,
                    total_leaves INT NOT NULL DEFAULT 12,
                    used_leaves INT NOT NULL DEFAULT 0,
                    remaining_leaves INT NOT NULL DEFAULT 12,
                    FOREIGN KEY (employee_id)
                        REFERENCES employees(employee_id)
                )
                """;

        String leaveRequestsTable = """
                CREATE TABLE IF NOT EXISTS leave_requests (
                    request_id INT PRIMARY KEY AUTO_INCREMENT,
                    employee_id INT NOT NULL,
                    leave_type VARCHAR(30) NOT NULL,
                    start_date DATE NOT NULL,
                    end_date DATE NOT NULL,
                    total_days INT NOT NULL,
                    reason VARCHAR(255),
                    status VARCHAR(20) NOT NULL DEFAULT 'Pending',
                    FOREIGN KEY (employee_id)
                        REFERENCES employees(employee_id)
                )
                """;

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.executeUpdate(employeesTable);
            System.out.println("Employees table created successfully!");

            stmt.executeUpdate(leaveBalanceTable);
            System.out.println("Leave balance table created successfully!");

            stmt.executeUpdate(leaveRequestsTable);
            System.out.println("Leave requests table created successfully!");

            System.out.println("\nDatabase setup completed successfully!");

        } catch (SQLException e) {
            System.out.println("Error creating database tables!");
            e.printStackTrace();
        }
    }
}
