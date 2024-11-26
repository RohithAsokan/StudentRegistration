package com.example.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import com.example.demo.service.exception.StudentAlreadyExistsException;
import com.example.demo.service.exception.StudentNotFoundException;

@RestController
@RequestMapping("/student-registration")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Student student) {
        try {
            String userId = studentService.login(student.getUsername(), student.getPassword());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", userId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } 
        catch (StudentNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } 
        catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/new-register")
    public ResponseEntity<String> addStudentDetails(@RequestBody Student student) {
        try {
            studentService.registerStudent(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful!");
        } 
        catch (StudentAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration.");
        }
    }

    @GetMapping("/my-profile/{userId}")
    public ResponseEntity<Student> getStudentByUserName(@PathVariable("userId") String userId) {
        try {
            Student student = studentService.getStudentById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(student);
        } 
        catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/edit-details/{userId}")
    public ResponseEntity<?> updateStudentDetails(@PathVariable String userId, @RequestBody Student student) {
        try {
            Student updatedStudent = studentService.updateStudentDetailsByUserId(userId, student);
            return ResponseEntity.ok(updatedStudent);
        } 
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", e.getMessage()));
        } 
        catch (StudentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
    	boolean available = studentService.isUsernameAvailable(username);
    	return ResponseEntity.ok(Collections.singletonMap("available", available));
    }
}
