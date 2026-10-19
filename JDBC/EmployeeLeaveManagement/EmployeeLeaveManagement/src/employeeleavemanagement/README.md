# STACKLY Employee Leave Management System

A Java-based Employee Leave Management System built using JDBC and MySQL. This application helps an organization manage employee registration, leave applications, approvals, leave balances, and leave history.

## Features

- Employee registration and employee listing
- Automatic STACKLY employee codes, such as `STK-26-01`
- Department selection: IT, HR, Finance, Marketing, and Operations
- Employee leave application
- Pending leave request management
- Approve or reject leave requests
- Automatic leave balance updates after approval
- View employee leave balances
- View leave history
- Input validation and JDBC transaction handling

## Technologies Used

- Java 21
- JDBC (Java Database Connectivity)
- MySQL
- MySQL Connector/J
- Eclipse IDE

## Project Structure

```text
EmployeeLeaveManagement/
├── src/
│   └── employeeleavemanagement/
│       ├── Main.java
│       ├── DBConnection.java
│       ├── DatabaseSetup.java
│       ├── EmployeeCodeSetup.java
│       ├── EmployeeManagement.java
│       ├── InitializeLeaveBalance.java
│       ├── LeaveManagement.java
│       ├── LeaveApproval.java
│       ├── LeaveBalance.java
│       └── LeaveHistory.java
└── README.md
```

## Database

Database name: `employee_leave_db`

Main tables:

- `employees` — stores employee details
- `leave_balance` — stores total, used, and remaining leave days
- `leave_requests` — stores leave applications, dates, reasons, and approval status

The application uses the internal numeric employee ID for database relationships and a STACKLY employee code for employee-facing operations.

## Setup and Installation

### Prerequisites

1. Install Java Development Kit (JDK) 21.
2. Install MySQL Server.
3. Install Eclipse IDE.
4. Add MySQL Connector/J to the project's build path.

### Configure the Database

1. Start MySQL Server.
2. Create the `employee_leave_db` database if it does not already exist.
3. Configure the database connection in `DBConnection.java`.
4. Run the project's database setup class if the required tables have not been created.

**Important:** Back up your database before making schema changes. Do not drop or recreate tables containing existing data.

### Run the Application

1. Open the project in Eclipse.
2. Open `Main.java` in the `employeeleavemanagement` package.
3. Right-click the file and select **Run As → Java Application**.
4. Select an option from the application menu.

## Application Menu

```text
1. Register Employee
2. View Employees
3. Apply Leave
4. View Pending Requests
5. Approve / Reject Leave
6. View Leave Balance
7. View Leave History
0. Exit
```

## JDBC Concepts Demonstrated

- Database connectivity using `DriverManager` and `Connection`
- SQL execution using `PreparedStatement`
- Retrieving data using `ResultSet`
- CRUD operations
- Table joins and foreign-key relationships
- Transactions using `commit()` and `rollback()`
- Resource management using try-with-resources

## Future Enhancements

- Employee and administrator login
- Role-based access control
- Email notifications for leave decisions
- Leave reports and dashboards
- A graphical user interface

## Security Note

Database credentials should be stored securely and must not be committed to a public repository. Configure your own local MySQL credentials before running the application.

## Author

Developed as a Java JDBC practice project to demonstrate employee management and leave management using Java and MySQL.