package com.example.demo.service;

import java.util.Optional;
//import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student getStudentByUsername(String username) {
        return studentRepository.findByUsername(username).orElse(null);
    }

    public Student registerStudent(Student student) {
        if (studentRepository.findById(student.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }
        
        return studentRepository.save(student);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudentDetailsByUserName(String username, Student student) {
        Optional<Student> existingStudentOpt = studentRepository.findByUsername(username);
        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            existingStudent.setName(student.getName());
            existingStudent.setPassword(student.getPassword());
            existingStudent.setPhone(student.getPhone());
            existingStudent.setEmail(student.getEmail());
            return studentRepository.save(existingStudent); 
        } 
        else return null;
    }
}
