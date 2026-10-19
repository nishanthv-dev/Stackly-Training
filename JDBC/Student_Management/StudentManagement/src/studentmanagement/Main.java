
package studentmanagement;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice = -1;

        do {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Student Registration");
            System.out.println("2. Course Management");
            System.out.println("3. Course Enrollment");
            System.out.println("4. Attendance Management");
            System.out.println("5. Marks Entry");
            System.out.println("6. Result Generation");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {




					case 1:
					    System.out.println("1. Register Student");
					    System.out.println("2. View All Students");
					    System.out.println("3. Delete Student");
					    System.out.print("Enter choice: ");
					
					    int studentChoice = Integer.parseInt(sc.nextLine());
					
					    if (studentChoice == 1) {
					        StudentManagement.registerStudent(sc);
					
					    } else if (studentChoice == 2) {
					        StudentManagement.viewStudents();
					
					    } else if (studentChoice == 3) {
					        System.out.print("Enter Student ID to delete: ");
					        int deleteId = Integer.parseInt(sc.nextLine());
					
					        StudentManagement.deleteStudent(deleteId);
					
					    } else {
					        System.out.println("Invalid choice!");
					    }
					    break;



				case 2:
				    System.out.println("\n----- COURSE MANAGEMENT -----");
				    System.out.println("1. Add Course");
				    System.out.println("2. View Courses");
				    System.out.println("3. View Student Enrollments");
				    System.out.print("Enter choice: ");
				
				    int courseChoice = Integer.parseInt(sc.nextLine());
				
				    switch (courseChoice) {
				        case 1:
				            CourseManagement.addCourse();
				            break;
				
				        case 2:
				            CourseManagement.viewCourses();
				            break;
				
				        case 3:
				            CourseManagement.viewEnrollments();
				            break;
				
				        default:
				            System.out.println("Invalid choice!");
				    }
				    break;
				    
				    
				case 3:
				    System.out.print("Enter Student ID: ");
				    int studentId = Integer.parseInt(sc.nextLine());

				    System.out.print("Enter Course ID: ");
				    int courseId = Integer.parseInt(sc.nextLine());

				    CourseManagement.enrollStudentTransaction(studentId, courseId);
				    break;
				
				case 4:
				    System.out.println("\n----- ATTENDANCE MANAGEMENT -----");
				    System.out.println("1. Mark Attendance");
				    System.out.println("2. View Attendance");
				    System.out.print("Enter choice: ");
				
				    int attendanceChoice = Integer.parseInt(sc.nextLine());
				
				    switch (attendanceChoice) {
				    case 1:
				        AttendanceManagement.displayEnrolledStudents();

				        System.out.print("Enter Enrollment ID: ");
				        int enrollmentId = sc.nextInt();
				        sc.nextLine();

				        System.out.print("Enter status (Present/Absent): ");
				        String status = sc.nextLine();

				        AttendanceManagement.markAttendance(
				                enrollmentId, status);
				        break;
				
				        case 2:
				            AttendanceManagement.viewAttendance();
				            break;
				
				        default:
				            System.out.println("Invalid choice!");
				    }
				    break;



					case 5:
					    System.out.println("\n----- MARKS MANAGEMENT -----");
					    System.out.println("1. Enter Marks");
					    System.out.println("2. View Marks");
					    System.out.println("3. Update Marks");
					    System.out.print("Enter choice: ");
					
					    int marksChoice = Integer.parseInt(sc.nextLine());
					
					    switch (marksChoice) {
					        case 1:
					            System.out.print("Enter Enrollment ID: ");
					            int markEnrollmentId =
					                    Integer.parseInt(sc.nextLine());
					
					            System.out.print("Enter Subject: ");
					            String subject = sc.nextLine();
					
					            System.out.print("Enter Marks Obtained: ");
					            double obtained =
					                    Double.parseDouble(sc.nextLine());
					
					            System.out.print("Enter Total Marks: ");
					            double total =
					                    Double.parseDouble(sc.nextLine());
					
					            MarksManagement.enterMarks(
					                    markEnrollmentId, subject, obtained, total);
					            break;
					
					        case 2:
					            MarksManagement.viewMarks();
					            break;


								case 3:
								    System.out.print("Enter Enrollment ID: ");
								    int updateEnrollmentId =
								            Integer.parseInt(sc.nextLine());
								
								    System.out.print("Enter Subject: ");
								    String updateSubject = sc.nextLine();
								
								    System.out.print("Enter New Marks Obtained: ");
								    double newObtained =
								            Double.parseDouble(sc.nextLine());
								
								    System.out.print("Enter Total Marks: ");
								    double newTotal =
								            Double.parseDouble(sc.nextLine());
								
								    MarksManagement.updateMarks(
								            updateEnrollmentId,
								            updateSubject,
								            newObtained,
								            newTotal);
								    break;


					        default:
					            System.out.println("Invalid choice!");
					    }


                case 6:
                    ResultGeneration.generateResults();
                    break;

                case 0:
                    System.out.println("Thank you for using the system!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 0);

        sc.close();
    }
}
