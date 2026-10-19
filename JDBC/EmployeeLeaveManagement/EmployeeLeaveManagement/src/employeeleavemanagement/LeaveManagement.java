
package employeeleavemanagement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class LeaveManagement {

    public void applyLeave(Scanner sc) {

        System.out.println("\n===== STACKLY APPLY FOR LEAVE =====");

        System.out.print("Enter Employee Code: ");
        String employeeCode = sc.nextLine().trim();

        System.out.print("Enter Leave Type (Casual/Sick/Earned): ");
        String leaveType = sc.nextLine().trim();

        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String startInput = sc.nextLine().trim();

        System.out.print("Enter End Date (YYYY-MM-DD): ");
        String endInput = sc.nextLine().trim();

        System.out.print("Enter Reason: ");
        String reason = sc.nextLine().trim();

        // Validate employee code
        if (employeeCode.isEmpty()) {
            System.out.println("Employee code cannot be empty!");
            return;
        }

        // Validate leave type
        if (!leaveType.equalsIgnoreCase("Casual")
                && !leaveType.equalsIgnoreCase("Sick")
                && !leaveType.equalsIgnoreCase("Earned")) {

            System.out.println(
                    "Invalid leave type! Choose Casual, Sick, or Earned.");
            return;
        }

        // Validate reason
        if (reason.isEmpty()) {
            System.out.println("Reason cannot be empty!");
            return;
        }

        try {
            Date startDate = Date.valueOf(startInput);
            Date endDate = Date.valueOf(endInput);

            // Validate date range
            if (endDate.before(startDate)) {
                System.out.println(
                        "Invalid dates! End date cannot be before start date.");
                return;
            }

            // Prevent past leave requests
            Date today = new Date(System.currentTimeMillis());

            if (startDate.before(today)) {
                System.out.println(
                        "Invalid date! Leave cannot start in the past.");
                return;
            }

            // Calculate inclusive leave days
            int totalDays = (int) ChronoUnit.DAYS.between(
                    startDate.toLocalDate(),
                    endDate.toLocalDate()) + 1;

            String employeeQuery = """
                    SELECT employee_id, employee_name
                    FROM employees
                    WHERE employee_code = ?
                    """;

            String balanceQuery = """
                    SELECT remaining_leaves
                    FROM leave_balance
                    WHERE employee_id = ?
                    """;

            String insertQuery = """
                    INSERT INTO leave_requests
                    (employee_id, leave_type, start_date, end_date,
                     total_days, reason, status)
                    VALUES (?, ?, ?, ?, ?, ?, 'Pending')
                    """;

            try (Connection con = DBConnection.getConnection()) {

                int employeeId;
                String employeeName;

                // Find employee using STACKLY employee code
                try (PreparedStatement ps =
                        con.prepareStatement(employeeQuery)) {

                    ps.setString(1, employeeCode);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            System.out.println(
                                    "Employee code not found!");
                            return;
                        }

                        employeeId = rs.getInt("employee_id");
                        employeeName = rs.getString("employee_name");
                    }
                }

                // Check leave balance
                try (PreparedStatement ps =
                        con.prepareStatement(balanceQuery)) {

                    ps.setInt(1, employeeId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            System.out.println(
                                    "Leave balance not found for this employee.");
                            return;
                        }

                        int remainingLeaves =
                                rs.getInt("remaining_leaves");

                        if (totalDays > remainingLeaves) {
                            System.out.println(
                                    "Insufficient leave balance!");
                            System.out.println(
                                    "Remaining leaves: " + remainingLeaves);
                            return;
                        }
                    }
                }

                // Insert leave request using internal numeric ID
                try (PreparedStatement ps =
                        con.prepareStatement(insertQuery)) {

                    ps.setInt(1, employeeId);
                    ps.setString(2, leaveType);
                    ps.setDate(3, startDate);
                    ps.setDate(4, endDate);
                    ps.setInt(5, totalDays);
                    ps.setString(6, reason);

                    ps.executeUpdate();

                    System.out.println(
                            "\nLeave applied successfully!");
                    System.out.println(
                            "Employee Code: " + employeeCode);
                    System.out.println(
                            "Employee Name: " + employeeName);
                    System.out.println("Leave Type: " + leaveType);
                    System.out.println("Total Days: " + totalDays);
                    System.out.println("Status: Pending");
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Invalid date! Enter a valid date in YYYY-MM-DD format.");

        } catch (SQLException e) {
            System.out.println("Failed to apply for leave!");
            e.printStackTrace();
        }
    }
}
