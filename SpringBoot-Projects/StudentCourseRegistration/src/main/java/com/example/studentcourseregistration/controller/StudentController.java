
package com.example.studentcourseregistration.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.studentcourseregistration.model.Student;
import com.example.studentcourseregistration.service.StudentService;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Register Student
    @PostMapping
    public ResponseEntity<Student> registerStudent(
            @Valid @RequestBody Student student) {

        Student savedStudent = studentService.registerStudent(student);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedStudent);
    }

    // View All Students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Find Student By ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable int id) {

        Student student = studentService.getStudentById(id);

        if (student != null) {
            return ResponseEntity.ok(student);
        }

        return ResponseEntity.notFound().build();
    }

    // Delete Student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable int id) {

        boolean deleted = studentService.deleteStudent(id);

        if (deleted) {
            return ResponseEntity.ok("Student deleted successfully");
        }

        return ResponseEntity.notFound().build();
    }
}
