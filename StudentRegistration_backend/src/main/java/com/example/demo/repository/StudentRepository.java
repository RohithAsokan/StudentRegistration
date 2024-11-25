package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {
	Optional<Student> findById(String username);
}
