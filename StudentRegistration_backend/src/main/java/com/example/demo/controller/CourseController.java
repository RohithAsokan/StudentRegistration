package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Course;
import com.example.demo.service.CourseService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {
	
	@Autowired
	private CourseService courseService;
	
	@PostMapping("/addcourse")
	public String addCourse(@RequestBody Course course) {
		return courseService.addCourse(course);
	}

    @GetMapping("/getcourses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
}
