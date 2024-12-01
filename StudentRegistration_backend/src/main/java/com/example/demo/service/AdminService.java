package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	private AdminRepository adminRepository;
	
	public String addCourse(Admin admin) {
		List<Admin> allCourses = adminRepository.findAll();
		for(Admin exixtingCourse : allCourses) {
			if(exixtingCourse.getCourseId().equals(admin.getCourseId())) {
				return "Courses with this ID already exists";
			}
		}
		adminRepository.save(admin);
		return "Course added Successfully";
	}
	
	public List<Admin> getAllCourses(){
		return adminRepository.findAll();
	}
	

}
