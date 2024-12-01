package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Course;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public boolean isUsernameAvailable(String username, String userId) {
        Student existingStudent = studentRepository.findByUsername(username);
        if (existingStudent == null) return true;
        if (userId != null && existingStudent.getId().equals(userId)) return true;
        return false;
    }
    
    public String login(String username, String password) {
        Student existingStudent = studentRepository.findByUsername(username);
        if (existingStudent == null || !existingStudent.getPassword().equals(password)) {
            return "Invalid username or password!";
        }
        return existingStudent.getId();
    }

    public String registerStudent(Student student) {
        if (!isUsernameAvailable(student.getUsername(), null)) {
            return "Username already exists!";
        }
        studentRepository.save(student);
        return "Registration successful!";
    }

    public Student getStudentById(String id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        return optionalStudent.orElse(null);
    }

    public Student updateStudentDetailsByUserId(String userId, Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepository.findById(userId);
        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();
            if (!existingStudent.getUsername().equals(updatedStudent.getUsername()) && !isUsernameAvailable(updatedStudent.getUsername(), userId)) {
                return null; 
            }
            existingStudent.setUsername(updatedStudent.getUsername());
            existingStudent.setPassword(updatedStudent.getPassword());
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setPhone(updatedStudent.getPhone());
            existingStudent.setEmail(updatedStudent.getEmail());
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
            for(Course enrolledCourse : enrolledCourses) {
            	if(enrolledCourse.getCourseId().equals(course.getCourseId())) {
            		return "Course already enrolled";
            	}
            }
            enrolledCourses.add(course);
            studentRepository.save(student);
            return "Successfully enrolled";
        }
        return "Error occured";
    }
    
    public List<Course> getEnrolledCourses(String userId) {
        Optional<Student> optionalStudent = studentRepository.findById(userId);
        if (optionalStudent.isPresent()) {
        	Student student = optionalStudent.get();
        	List<Course> enrolledCourses = student.getEnrolledCourses();
        	if(enrolledCourses != null) return enrolledCourses;
        }
        return new ArrayList<>();
    }
}
