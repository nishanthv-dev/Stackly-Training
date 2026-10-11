package student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StudentFileService {

    public void saveStudents(StudentService service) {

        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter("students.txt"));

            for (Student student : service.getStudents().values()) {

                bw.write(student.getId() + "," +
                        student.getName() + "," +
                        student.getEmail() + "," +
                        student.getMarks() + "," +
                        student.getAttendance() + "," +
                        student.getStatus() + "," +
                        student.getCourse().getCourseId() + "," +
                        student.getCourse().getCourseName());

                bw.newLine();
            }

            bw.close();

            System.out.println("Student details saved successfully");

        } catch (IOException e) {

            System.out.println("Error while saving students");

        }

    }

    public void loadStudents(StudentService service) {

        try {

            BufferedReader br = new BufferedReader(new FileReader("students.txt"));

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);

                String name = data[1];

                String email = data[2];

                double marks = Double.parseDouble(data[3]);

                int attendance = Integer.parseInt(data[4]);

                StudentStatus status = StudentStatus.valueOf(data[5]);

                int courseId = Integer.parseInt(data[6]);

                String courseName = data[7];

                Course course = new Course(courseId, courseName);

                Student student = new Student(
                        id,
                        name,
                        email,
                        marks,
                        attendance,
                        status,
                        course
                );

                try {

                    service.addStudent(student);

                } catch (StudentException e) {

                    System.out.println(e.getMessage());

                }

            }

            br.close();

            System.out.println("Student details loaded successfully");

        } catch (IOException e) {

            System.out.println("Error while loading students");

        }

    }

}