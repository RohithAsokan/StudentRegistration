package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.demo.model.Course;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("/student-registration")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public String login(@RequestBody Student student) {
        return studentService.login(student.getUsername(), student.getPassword());
    }

    @PostMapping("/register")
    public String registerStudent(@RequestBody Student student) {
        return studentService.registerStudent(student);
    }

    @GetMapping("/profile/{userId}")
    public Student getStudentById(@PathVariable String userId) {
        return studentService.getStudentById(userId);
    }

    @PutMapping("/update/{userId}")
    public Student updateStudentDetails(@PathVariable String userId, @RequestBody Student student) {
        return studentService.updateStudentDetailsByUserId(userId, student);
    }
    
    @GetMapping("/check-username")
    public boolean checkUsernameAvailability(@RequestParam String username,  String userId) {
        return studentService.isUsernameAvailable(username, userId);
    }

    @PostMapping("/{userId}/enrollcourse")
    public String enrollCourse(@PathVariable String userId, @RequestBody Course course) {
    	return studentService.enrollCourse(userId, course);
    }

    @GetMapping("/{userId}/enrolledcourses")
    public List<Course> getEnrolledCourses(@PathVariable String userId) {
        return studentService.getEnrolledCourses(userId);
    }
}
