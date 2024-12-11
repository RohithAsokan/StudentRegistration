package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.model.Course;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JavaMailSender mailSender;

	public boolean isEmailIdAvailable(String emailId, String userId) {
		Student existingStudent = studentRepository.findByEmailId(emailId);
		if (existingStudent == null)
			return true;
		if (userId != null && existingStudent.getId().equals(userId))
			return true;
		return false;
	}

	public String login(String emailId, String password) {
		Student existingStudent = studentRepository.findByEmailId(emailId);
		if (existingStudent == null || !existingStudent.getPassword().equals(password)) {
			return "Invalid username or password!";
		}
		return existingStudent.getId();
	}

	public String registerStudent(Student student) {
		if (!isEmailIdAvailable(student.getEmailId(), null)) {
			return "Email Id already exists!";
		}
		studentRepository.save(student);
		return "Registration successful!";
	}

	public Optional<Student> getStudentById(String userId) {
		return studentRepository.findById(userId);
	}

	public Student updateStudentDetailsByUserId(String userId, Student updatedStudent) {
		Optional<Student> optionalStudent = studentRepository.findById(userId);
		if (optionalStudent.isPresent()) {
			Student existingStudent = optionalStudent.get();
			if (!existingStudent.getEmailId().equals(updatedStudent.getEmailId())
					&& !isEmailIdAvailable(updatedStudent.getEmailId(), userId)) {
				return null;
			}
			existingStudent.setFirstName(updatedStudent.getFirstName());
			existingStudent.setLastName(updatedStudent.getLastName());
			existingStudent.setEmailId(updatedStudent.getEmailId());
			existingStudent.setPassword(updatedStudent.getPassword());
			existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
			existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
			existingStudent.setAddress(updatedStudent.getAddress());
			return studentRepository.save(existingStudent);
		}
		return null;
	}

	public String enrollCourse(String userId, Course course) {
		Optional<Student> studentOpt = studentRepository.findById(userId);
		if (studentOpt.isPresent()) {
			Student student = studentOpt.get();
			List<Course> enrolledCourses = student.getEnrolledCourses();
			if (enrolledCourses == null) {
				enrolledCourses = new ArrayList<>();
				student.setEnrolledCourses(enrolledCourses);
			}
			for (Course enrolledCourse : enrolledCourses) {
				if (enrolledCourse.getCourseId().equals(course.getCourseId())) {
					return "Course already enrolled";
				}
			}
			enrolledCourses.add(course);
			studentRepository.save(student);
			sendEnrollmentEmail(student.getEmailId(), course.getCourseName());
			return "Successfully enrolled to "+ course.getCourseName();
		}
		return "Error occured";
	}
	
	public void sendEnrollmentEmail(String email, String courseName) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Course Enrollment Details");
		message.setText("You have successfully enrolled in the course -  " + courseName + ".");
		mailSender.send(message);
	}

	public List<Course> getEnrolledCourses(String userId) {
		Optional<Student> optionalStudent = studentRepository.findById(userId);
		if (optionalStudent.isPresent()) {
			Student student = optionalStudent.get();
			List<Course> enrolledCourses = student.getEnrolledCourses();
			if (enrolledCourses != null)
				return enrolledCourses;
		}
		return new ArrayList<>();
	}

}
