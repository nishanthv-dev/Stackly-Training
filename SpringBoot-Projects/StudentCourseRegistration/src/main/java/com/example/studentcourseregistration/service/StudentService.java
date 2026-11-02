
package com.example.studentcourseregistration.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.studentcourseregistration.model.Student;

@Service
public class StudentService {

    private List<Student> students = new ArrayList<>();

    // Register Student
    public Student registerStudent(Student student) {

        student.setStudentId(students.size() + 1);
        students.add(student);

        return student;
    }

    // View All Students
    public List<Student> getAllStudents() {
        return students;
    }

    // Find Student By ID
    public Student getStudentById(int studentId) {

        for (Student student : students) {

            if (student.getStudentId() == studentId) {
                return student;
            }
        }

        return null;
    }

    // Delete Student
    public boolean deleteStudent(int studentId) {

        Student student = getStudentById(studentId);

        if (student != null) {
            students.remove(student);
            return true;
        }

        return false;
    }
}
