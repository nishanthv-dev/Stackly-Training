
package employeeleavemanagement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class EmployeeManagement {

    private static final String[] DEPARTMENTS = {
        "IT", "HR", "Finance", "Marketing", "Operations"
    };

    // Generate the next employee code for the current year
    private String generateEmployeeCode(Connection con)
            throws SQLException {

        String prefix = String.format(
                "STK-%02d-", LocalDate.now().getYear() % 100);

        String sql = """
                SELECT employee_code
                FROM employees
                WHERE employee_code LIKE ?
                """;

        int maxNumber = 0;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, prefix + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String code = rs.getString("employee_code");

                    if (code != null) {
                        String[] parts = code.split("-");

                        if (parts.length == 3) {
                            try {
                                int number =
                                        Integer.parseInt(parts[2]);

                                if (number > maxNumber) {
                                    maxNumber = number;
                                }
                            } catch (NumberFormatException ignored) {
                                // Ignore incorrectly formatted codes
                            }
                        }
                    }
                }
            }
        }

        return prefix + String.format("%02d", maxNumber + 1);
    }

    // Assign codes to existing employees that do not have one
    private void assignMissingEmployeeCodes(Connection con)
            throws SQLException {

        String selectSql = """
                SELECT employee_id
                FROM employees
                WHERE employee_code IS NULL
                ORDER BY employee_id
                """;

        try (PreparedStatement select =
                     con.prepareStatement(selectSql);
             ResultSet rs = select.executeQuery()) {

            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                String employeeCode = generateEmployeeCode(con);

                String updateSql = """
                        UPDATE employees
                        SET employee_code = ?
                        WHERE employee_id = ?
                          AND employee_code IS NULL
                        """;

                try (PreparedStatement update =
                             con.prepareStatement(updateSql)) {

                    update.setString(1, employeeCode);
                    update.setInt(2, employeeId);
                    update.executeUpdate();
                }
            }
        }
    }

    public void registerEmployee(Scanner sc) {

        System.out.println("\n========================================");
        System.out.println("     STACKLY EMPLOYEE REGISTRATION");
        System.out.println("========================================");

        System.out.print("\nEnter Employee Name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Employee name cannot be empty!");
            return;
        }

        System.out.println("\nSelect Department:");

        for (int i = 0; i < DEPARTMENTS.length; i++) {
            System.out.println((i + 1) + ". " + DEPARTMENTS[i]);
        }

        System.out.print("\nEnter Department Choice: ");

        int departmentChoice;

        try {
            departmentChoice =
                    Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid department number!");
            return;
        }

        if (departmentChoice < 1
                || departmentChoice > DEPARTMENTS.length) {
            System.out.println("Invalid department choice!");
            return;
        }

        String department = DEPARTMENTS[departmentChoice - 1];

        System.out.print("\nEnter Email: ");
        String email = sc.nextLine().trim();

        if (email.isEmpty() || !email.contains("@")) {
            System.out.println("Please enter a valid email address!");
            return;
        }

        System.out.print("Enter Joining Date (YYYY-MM-DD): ");
        String joiningDateInput = sc.nextLine().trim();

        try {
            Date joiningDate = Date.valueOf(joiningDateInput);

            String employeeSql = """
                    INSERT INTO employees
                    (employee_code, employee_name, department,
                     email, joining_date)
                    VALUES (?, ?, ?, ?, ?)
                    """;

            String balanceSql = """
                    INSERT INTO leave_balance
                    (employee_id, total_leaves, used_leaves,
                     remaining_leaves)
                    VALUES (?, 12, 0, 12)
                    """;

            try (Connection con = DBConnection.getConnection()) {

                con.setAutoCommit(false);

                try {
                    // Preserve existing employees and assign their codes
                    assignMissingEmployeeCodes(con);

                    String employeeCode =
                            generateEmployeeCode(con);

                    int employeeId;

                    try (PreparedStatement ps = con.prepareStatement(
                            employeeSql,
                            Statement.RETURN_GENERATED_KEYS)) {

                        ps.setString(1, employeeCode);
                        ps.setString(2, name);
                        ps.setString(3, department);
                        ps.setString(4, email);
                        ps.setDate(5, joiningDate);

                        ps.executeUpdate();

                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (!rs.next()) {
                                throw new SQLException(
                                        "Employee ID was not generated.");
                            }

                            employeeId = rs.getInt(1);
                        }
                    }

                    try (PreparedStatement ps =
                            con.prepareStatement(balanceSql)) {

                        ps.setInt(1, employeeId);
                        ps.executeUpdate();
                    }

                    con.commit();

                    System.out.println("\n----------------------------------------");
                    System.out.println(
                            "Employee registered successfully!");
                    System.out.println();
                    System.out.println("Employee ID    : " + employeeCode);
                    System.out.println("Employee Name  : " + name);
                    System.out.println("Department     : " + department);
                    System.out.println("Email          : " + email);
                    System.out.println("Joining Date   : " + joiningDate);
                    System.out.println("Total Leaves   : 12");
                    System.out.println("Used Leaves    : 0");
                    System.out.println("Remaining      : 12");
                    System.out.println("----------------------------------------");

                } catch (SQLException e) {
                    con.rollback();
                    throw e;
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid date! Use a valid date in YYYY-MM-DD format.");

        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    public void viewEmployees() {

        String sql = """
                SELECT employee_id, employee_code, employee_name,
                       department, email, joining_date
                FROM employees
                ORDER BY employee_id
                """;

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                assignMissingEmployeeCodes(con);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                System.out.println("\n===== STACKLY EMPLOYEE LIST =====");
                System.out.printf("%-14s %-20s %-15s %-30s %-15s%n",
                        "Employee Code", "Name", "Department",
                        "Email", "Joining Date");

                while (rs.next()) {
                    System.out.printf("%-14s %-20s %-15s %-30s %-15s%n",
                            rs.getString("employee_code"),
                            rs.getString("employee_name"),
                            rs.getString("department"),
                            rs.getString("email"),
                            rs.getDate("joining_date"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to display employees!");
            e.printStackTrace();
        }
    }
}
