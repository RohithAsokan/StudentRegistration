package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.exception.StudentAlreadyExistsException;
import com.example.demo.service.exception.StudentNotFoundException;


@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public boolean isUsernameAvailable(String username) {
        Student student = studentRepository.findByUsername(username);
        return student == null;
    }

    public String login(String username, String password) throws StudentNotFoundException {
        Student existingStudent = studentRepository.findByUsername(username);
        if (existingStudent == null || !existingStudent.getPassword().equals(password)) {
            throw new StudentNotFoundException("Invalid username or password!");
        }
        return existingStudent.getId();
    }

    public void registerStudent(Student student) throws StudentAlreadyExistsException {
        if (!isUsernameAvailable(student.getUsername())) {
            throw new StudentAlreadyExistsException("Username already exists!");
        }
        studentRepository.save(student);
    }

    public Student getStudentById(String id) throws StudentNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            return optionalStudent.get();
        } 
        else {
            throw new StudentNotFoundException("Student not found!");
        }
    }

    public Student updateStudentDetailsByUserId(String userId, Student updatedStudent) throws StudentNotFoundException {
        Optional<Student> optionalStudent = studentRepository.findById(userId);
        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();
            if (!existingStudent.getUsername().equals(updatedStudent.getUsername()) && !isUsernameAvailable(updatedStudent.getUsername())) {
                throw new IllegalArgumentException("Username already exists!");
            }
            existingStudent.setUsername(updatedStudent.getUsername());
            existingStudent.setPassword(updatedStudent.getPassword());
            existingStudent.setName(updatedStudent.getName());
            existingStudent.setPhone(updatedStudent.getPhone());
            existingStudent.setEmail(updatedStudent.getEmail());

            return studentRepository.save(existingStudent);
        } 
        else {
            throw new StudentNotFoundException("Student not found!");
        }
    }
}
