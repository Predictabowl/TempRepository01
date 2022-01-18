package com.example.demo.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public Employee oneEmployee(@PathVariable long id){
		Employee employeeById = employeeService.getEmployeeById(id);
		if (Objects.isNull(employeeById))
			throw new ResourceNotFoundException();
		return employeeById;
	}
	
	@PostMapping("/new")
	public Employee newEmployee(@RequestBody Employee employee) {
		return employeeService.insertNewEmployee(employee);
	}
	
	@PutMapping("/update/{id}")
	public Employee updateEmployee(@PathVariable long id, @RequestBody Employee employee) {
		return employeeService.updateEmployeeById(id, employee);
	}
}
