package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

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
import com.example.demo.model.Course;
import com.example.demo.service.JWTService;
import com.example.demo.service.StudentService;

@RestController
@RequestMapping("/student-registration")
@CrossOrigin(origins = "http://localhost:3000")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@Autowired
	private JWTService jwtService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Student student) {
		String userId = studentService.login(student.getEmailId(), student.getPassword());
		if (userId.equals("Invalid username or password!")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
		}
		String token = jwtService.generateToken(userId);
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerStudent(@RequestBody Student student) {
		String response = studentService.registerStudent(student);
		if (response.equals("Email Id already exists!")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/profile/{userId}")
	public ResponseEntity<?> getStudentById(@PathVariable String userId) {
	    Optional<Student> student = studentService.getStudentById(userId);
	    if (student.isPresent()) {
	        return ResponseEntity.ok(student.get());
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
	    }
	}


	@PutMapping("/update/{userId}")
	public Student updateStudentDetails(@PathVariable String userId, @RequestBody Student student) {
		return studentService.updateStudentDetailsByUserId(userId, student);
	}

	@GetMapping("/check-emailId")
	public boolean checkUsernameAvailability(@RequestParam String emailId, String userId) {
		return studentService.isEmailIdAvailable(emailId, userId);
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
