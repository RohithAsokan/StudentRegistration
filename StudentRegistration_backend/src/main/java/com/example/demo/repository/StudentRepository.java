package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByUsername(String username);
    Optional<Student> findStudentById(String studentId);
}
