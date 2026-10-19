
package employeeleavemanagement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeCodeSetup {

    public static void main(String[] args) {

        String checkSql = """
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = 'employee_leave_db'
                  AND table_name = 'employees'
                  AND column_name = 'employee_code'
                """;

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement()) {

            boolean columnExists;

            try (ResultSet rs = stmt.executeQuery(checkSql)) {
                rs.next();
                columnExists = rs.getInt(1) > 0;
            }

            if (columnExists) {
                System.out.println(
                        "employee_code column already exists!");
            } else {
                stmt.executeUpdate("""
                        ALTER TABLE employees
                        ADD COLUMN employee_code VARCHAR(20) UNIQUE
                        """);

                System.out.println(
                        "employee_code column added successfully!");
            }

        } catch (SQLException e) {
            System.out.println("Database setup failed: "
                    + e.getMessage());
        }
    }
}
