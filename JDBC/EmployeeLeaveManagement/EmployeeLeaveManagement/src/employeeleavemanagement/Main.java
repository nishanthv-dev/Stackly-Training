package employeeleavemanagement;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        EmployeeManagement employeeManagement = new EmployeeManagement();

        LeaveManagement leaveManagement = new LeaveManagement();

        LeaveApproval leaveApproval = new LeaveApproval();

        int choice;

        do {
            System.out.println("\n========================================");
            System.out.println("       STACKLY EMPLOYEE PORTAL");
            System.out.println("========================================");
            System.out.println("1. Register Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Apply Leave");
            System.out.println("4. View Pending Requests");
            System.out.println("5. Approve / Reject Leave");
            System.out.println("6. View Leave Balance");
            System.out.println("7. View Leave History");
            System.out.println("0. Exit");
            System.out.println("========================================");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println(
                        "Invalid input! Please enter a number.");
                sc.nextLine();
                choice = -1;
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {

                case 1:
                    employeeManagement.registerEmployee(sc);
                    break;

                case 2:
                    employeeManagement.viewEmployees();
                    break;

                case 3:
                    leaveManagement.applyLeave(sc);
                    break;

                case 4:
                    leaveApproval.viewPendingRequests();
                    break;

                case 5:
                    leaveApproval.processLeave(sc);
                    break;

                case 6:
                    LeaveBalance.viewBalance(sc);
                    break;

                case 7:
                    LeaveHistory.viewHistory(sc);
                    break;

                case 0:
                    System.out.println(
                            "\nThank you for using STACKLY Employee Portal!");
                    System.out.println("Have a great day!");
                    break;

                default:
                    System.out.println(
                            "Invalid choice! Please select a number from 0 to 7.");
            }

        } while (choice != 0);

        sc.close();
    }
}
