package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;

@Service
public class EmployeeService {

	private static final String TEMP_IMPLEMENTATION = "Method not yet implemented.";

	public List<Employee> getAllEmployees() {
		throw new UnsupportedOperationException(TEMP_IMPLEMENTATION);
	}

	public Employee getEmployeeById(long l) {
		throw new UnsupportedOperationException(TEMP_IMPLEMENTATION);		
	}

	public void insertNewEmployee(Employee employee) {
		throw new UnsupportedOperationException(TEMP_IMPLEMENTATION);
	}

	public void updateEmployeeById(long id, Employee employee) {
		throw new UnsupportedOperationException(TEMP_IMPLEMENTATION);
	}

}
