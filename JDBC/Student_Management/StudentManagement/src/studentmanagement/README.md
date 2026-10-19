
# Student Management System using Java JDBC and MySQL

## Project Description
The Student Management System is a console-based Java application
developed using JDBC and MySQL. It helps manage student details,
courses, enrollments, attendance, marks, and results.

## Features
- Student Registration with automatically generated Student ID
- Course Management
- Course Enrollment
- Attendance Management (Present/Absent)
- Marks Entry, View, and Update
- Automatic Result Generation
- Percentage Calculation
- PASS / FAIL status based on percentage
- Pending status when marks have not been entered
- MySQL database connectivity using JDBC

## Technologies Used
- Java
- JDBC (Java Database Connectivity)
- MySQL
- MySQL Workbench
- Eclipse IDE

## Database Tables
- students
- courses
- enrollments
- attendance
- marks

## Requirements
- Java JDK
- MySQL Server
- MySQL Workbench (optional, for database management)
- Eclipse IDE
- MySQL JDBC Driver

## Database Setup

1. Start MySQL Server.
2. Open MySQL Workbench.
3. Create the project database:

   CREATE DATABASE student_management;

4. Select the database:

   USE student_management;

5. Ensure the required tables and relationships are created.
   Use the project's SQL scripts if available.

## JDBC Configuration

Open DBConnection.java and configure the database connection
with your MySQL username, password, and database name.

Example:

- Database: student_management
- Username: your MySQL username
- Password: your MySQL password

Make sure the MySQL JDBC Driver is added to the project.

Do not upload real database passwords to GitHub.

## How to Run the Project

1. Open Eclipse IDE.
2. Import or open the project.
3. Configure MySQL connection details in DBConnection.java.
4. Ensure the database and required tables are available.
5. Run Main.java.
6. Select an option from the console menu.
7. Follow the instructions to manage students, courses,
   enrollments, attendance, marks, and results.

## Result Calculation

Percentage = (Total Marks Obtained / Total Maximum Marks) * 100

- Percentage >= 40: PASS
- Percentage < 40: FAIL
- Marks not entered: Pending

## Author
Nishanth P R
