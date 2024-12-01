package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Admin;
import com.example.demo.service.AdminService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@PostMapping("/addcourse")
	public String addCourse(@RequestBody Admin admin) {
		return adminService.addCourse(admin);
	}

    @GetMapping("/getcourses")
    public List<Admin> getAllCourses() {
        return adminService.getAllCourses();
    }
}
