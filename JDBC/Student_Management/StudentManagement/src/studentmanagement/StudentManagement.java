
package studentmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentManagement {



public static void registerStudent(Scanner sc) {

    String query = "INSERT INTO students "
            + "(student_name, age, gender, email, phone) "
            + "VALUES (?, ?, ?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(
                 query, java.sql.Statement.RETURN_GENERATED_KEYS)) {

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        System.out.print("Enter age: ");
        int age = Integer.parseInt(sc.nextLine());

        System.out.print("Enter gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter email: ");
        String email = sc.nextLine();

        System.out.print("Enter phone: ");
        String phone = sc.nextLine();

        ps.setString(1, name);
        ps.setInt(2, age);
        ps.setString(3, gender);
        ps.setString(4, email);
        ps.setString(5, phone);

        int rows = ps.executeUpdate();

        if (rows > 0) {

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    int studentId = rs.getInt(1);

                    System.out.println(
                            "\nStudent Registered Successfully!");

                    System.out.println(
                            "\n===== REGISTERED STUDENT DETAILS =====");
                    System.out.println("Student ID   : " + studentId);
                    System.out.println("Student Name : " + name);
                    System.out.println("Age          : " + age);
                    System.out.println("Gender       : " + gender);
                    System.out.println("Email        : " + email);
                    System.out.println("Phone        : " + phone);
                    System.out.println(
                            "======================================");
                }
            }

        } else {
            System.out.println("Student registration failed!");
        }

    } catch (SQLException | NumberFormatException e) {
        System.out.println("Registration failed: " + e.getMessage());
    }
}



    public static void viewStudents() {
        String query = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n----- Student Details -----");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("student_id"));
                System.out.println("Name: " + rs.getString("student_name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("---------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Unable to retrieve students: " + e.getMessage());
        }
    }

    
    

public static void deleteStudent(int studentId) {

    String checkQuery =
            "SELECT COUNT(*) FROM enrollments WHERE student_id = ?";

    String deleteQuery =
            "DELETE FROM students WHERE student_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement checkPs = con.prepareStatement(checkQuery)) {

        checkPs.setInt(1, studentId);

        try (ResultSet rs = checkPs.executeQuery()) {
            rs.next();

            if (rs.getInt(1) > 0) {
                System.out.println(
                    "Cannot delete student: enrollment records exist.");
                return;
            }
        }

        try (PreparedStatement deletePs =
                     con.prepareStatement(deleteQuery)) {

            deletePs.setInt(1, studentId);

            int rows = deletePs.executeUpdate();

            if (rows > 0) {
                System.out.println("Student Deleted Successfully!");
            } else {
                System.out.println("Student ID not found!");
            }
        }

    } catch (SQLException e) {
        System.out.println("Error deleting student: " + e.getMessage());
    }
}

    
    
  
}
