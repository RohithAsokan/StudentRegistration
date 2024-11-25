package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("/student-registration")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Student student) {
    	Student existingStudent = studentService.getStudentByUsername(student.getUsername());
    	if (existingStudent == null || !existingStudent.getPassword().equals(student.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Login Successful");
    }

    @PostMapping("/new-register")
    public ResponseEntity<String> addStudentDetails(@RequestBody Student student) {
        try {
            studentService.registerStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful!");
        } 
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists!");
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration.");
        }
    }
    
    @GetMapping("/my-profile/{userName}")
    public Student getStudentByUserName(@PathVariable("userName") String userName) {
    	return studentService.getStudentByUsername(userName);
    }
    
    @PutMapping("/edit-details/{username}")
    public ResponseEntity<Student> updateStudentDetailsByUserName(@PathVariable("username") String username, @RequestBody Student student) {
        Student updatedStudent = studentService.updateStudentDetailsByUserName(username, student);
        if (updatedStudent != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedStudent);
        } 
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
