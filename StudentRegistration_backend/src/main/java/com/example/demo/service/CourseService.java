package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;

@Service
public class CourseService {
	
	@Autowired
	private CourseRepository courseRepository;
	
	public String addCourse(Course course) {
		List<Course> allCourses = courseRepository.findAll();
		for(Course exixtingCourse : allCourses) {
			if(exixtingCourse.getCourseId().equals(course.getCourseId())) {
				return "Courses with this ID already exists";
			}
		}
		courseRepository.save(course);
		return "Course added Successfully";
	}
	
	public List<Course> getAllCourses(){
		return courseRepository.findAll();
	}
	

}
