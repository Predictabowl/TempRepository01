package com.example.demo.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.demo.jpa.repositories.EmployeeRepository;
import com.example.demo.model.Employee;
import com.example.demo.model.dto.EmployeeDTO;

@Service
public class EmployeeService {

	private ModelMapper mapper;	
	private EmployeeRepository employeeRepository;
	
	public EmployeeService(EmployeeRepository employeeRepository, ModelMapper mapper) {
		super();
		this.employeeRepository = employeeRepository;
		this.mapper = mapper;
	}

	public List<Employee> getAllEmployees(){
		return employeeRepository.findAll();
	}

	public Employee getEmployeeById(long id) {
		return employeeRepository.findById(id).orElse(null);
	}

	public Employee insertNewEmployee(EmployeeDTO employee) {
		employee.setId(null);
		Employee toSave = mapper.map(employee, Employee.class);
		return employeeRepository.save(toSave);
	}

	public Employee updateEmployeeById(long id, EmployeeDTO employee) {
		employee.setId(id);
		Employee replacement = mapper.map(employee, Employee.class);
		return employeeRepository.save(replacement);
	}

	public void deleteEmployeeById(long id) {
		employeeRepository.deleteById(id);;
	}
}
