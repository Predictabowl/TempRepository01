package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;
import com.example.demo.repositories.EmployeeRepository;

@Service
public class EmployeeService {

	private EmployeeRepository employeeRepository;
	
	
	public EmployeeService(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}

	public List<Employee> getAllEmployees(){
		return employeeRepository.findAll();
	}

	public Employee getEmployeeById(long id) {
		return employeeRepository.findById(id).orElse(null);
	}

	public Employee insertNewEmployee(Employee toSave) {
		toSave.setId(null);
		return employeeRepository.save(toSave);
	}

	public Employee updateEmployeeById(long id, Employee replacement) {
		replacement.setId(id);
		return employeeRepository.save(replacement);
	}
}
