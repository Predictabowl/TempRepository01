package com.example.demo.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.services.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping
	public List<Employee> allEmployees(){
		return employeeService.getAllEmployees();
	}
	
	@GetMapping("/{id}")
	public Employee oneEmployee(@PathVariable long id) throws ResourceNotFoundException {
		Employee employeeById = employeeService.getEmployeeById(id);
		if (Objects.isNull(employeeById))
			throw new ResourceNotFoundException();
		return employeeById;
	}
}
