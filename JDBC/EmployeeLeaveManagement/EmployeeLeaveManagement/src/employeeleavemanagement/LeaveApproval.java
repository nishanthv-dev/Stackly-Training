
package employeeleavemanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LeaveApproval {

    public void viewPendingRequests() {

        String query = """
                SELECT lr.request_id, e.employee_code,
                       e.employee_name, e.department,
                       lr.leave_type, lr.start_date, lr.end_date,
                       lr.total_days, lr.reason, lr.status
                FROM leave_requests lr
                JOIN employees e
                    ON lr.employee_id = e.employee_id
                WHERE lr.status = 'Pending'
                ORDER BY lr.request_id
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println(
                    "\n===== STACKLY PENDING LEAVE REQUESTS =====");

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("----------------------------------------");
                System.out.println(
                        "Request ID: " + rs.getInt("request_id"));
                System.out.println(
                        "Employee Code: " + rs.getString("employee_code"));
                System.out.println(
                        "Employee Name: " + rs.getString("employee_name"));
                System.out.println(
                        "Department: " + rs.getString("department"));
                System.out.println(
                        "Leave Type: " + rs.getString("leave_type"));
                System.out.println(
                        "Start Date: " + rs.getDate("start_date"));
                System.out.println(
                        "End Date: " + rs.getDate("end_date"));
                System.out.println(
                        "Total Days: " + rs.getInt("total_days"));
                System.out.println(
                        "Reason: " + rs.getString("reason"));
                System.out.println(
                        "Status: " + rs.getString("status"));
            }

            if (!found) {
                System.out.println("No pending leave requests.");
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve leave requests.");
            e.printStackTrace();
        }
    }

    public void processLeave(Scanner sc) {

        System.out.println("\n===== STACKLY APPROVE / REJECT LEAVE =====");

        System.out.print("Enter Request ID: ");
        int requestId;

        try {
            requestId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Request ID must be a number!");
            return;
        }

        System.out.print("Enter decision (Approve/Reject): ");
        String decision = sc.nextLine().trim();

        if (!decision.equalsIgnoreCase("Approve")
                && !decision.equalsIgnoreCase("Reject")) {
            System.out.println("Invalid decision! Choose Approve or Reject.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                String requestQuery = """
                        SELECT lr.employee_id, lr.total_days, lr.status,
                               e.employee_code, e.employee_name,
                               e.department
                        FROM leave_requests lr
                        JOIN employees e
                            ON lr.employee_id = e.employee_id
                        WHERE lr.request_id = ?
                        FOR UPDATE
                        """;

                int employeeId;
                int totalDays;
                String status;
                String employeeCode;
                String employeeName;
                String department;

                try (PreparedStatement ps =
                        con.prepareStatement(requestQuery)) {

                    ps.setInt(1, requestId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            System.out.println("Request ID not found!");
                            con.rollback();
                            return;
                        }

                        employeeId = rs.getInt("employee_id");
                        totalDays = rs.getInt("total_days");
                        status = rs.getString("status");
                        employeeCode = rs.getString("employee_code");
                        employeeName = rs.getString("employee_name");
                        department = rs.getString("department");
                    }
                }

                System.out.println("\n===== REQUEST DETAILS =====");
                System.out.println("Employee Code: " + employeeCode);
                System.out.println("Employee Name: " + employeeName);
                System.out.println("Department: " + department);
                System.out.println("Total Days: " + totalDays);
                System.out.println("Current Status: " + status);

                if (!"Pending".equalsIgnoreCase(status)) {
                    System.out.println(
                            "This request has already been processed: "
                                    + status);
                    con.rollback();
                    return;
                }

                if (decision.equalsIgnoreCase("Approve")) {

                    String balanceQuery = """
                            SELECT remaining_leaves
                            FROM leave_balance
                            WHERE employee_id = ?
                            FOR UPDATE
                            """;

                    try (PreparedStatement ps =
                            con.prepareStatement(balanceQuery)) {

                        ps.setInt(1, employeeId);

                        try (ResultSet rs = ps.executeQuery()) {

                            if (!rs.next()) {
                                throw new SQLException(
                                        "Leave balance record not found.");
                            }

                            int remaining =
                                    rs.getInt("remaining_leaves");

                            if (totalDays > remaining) {
                                System.out.println(
                                        "Insufficient leave balance!");
                                con.rollback();
                                return;
                            }
                        }
                    }

                    String balanceUpdate = """
                            UPDATE leave_balance
                            SET used_leaves = used_leaves + ?,
                                remaining_leaves = remaining_leaves - ?
                            WHERE employee_id = ?
                            """;

                    try (PreparedStatement ps =
                            con.prepareStatement(balanceUpdate)) {

                        ps.setInt(1, totalDays);
                        ps.setInt(2, totalDays);
                        ps.setInt(3, employeeId);

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException(
                                    "Unable to update leave balance.");
                        }
                    }
                }

                String newStatus =
                        decision.equalsIgnoreCase("Approve")
                                ? "Approved" : "Rejected";

                String statusUpdate = """
                        UPDATE leave_requests
                        SET status = ?
                        WHERE request_id = ?
                          AND status = 'Pending'
                        """;

                try (PreparedStatement ps =
                        con.prepareStatement(statusUpdate)) {

                    ps.setString(1, newStatus);
                    ps.setInt(2, requestId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Unable to update request status.");
                    }
                }

                con.commit();

                System.out.println("\nLeave request " +
                        newStatus.toLowerCase() + " successfully!");
                System.out.println("Employee Code: " + employeeCode);
                System.out.println("Employee Name: " + employeeName);

                if (newStatus.equals("Approved")) {
                    System.out.println(
                            totalDays + " leave day(s) deducted.");
                } else {
                    System.out.println(
                            "Leave balance was not changed.");
                }

            } catch (SQLException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;
            }

        } catch (SQLException e) {
            System.out.println("Unable to process leave request.");
            e.printStackTrace();
        }
    }
}
