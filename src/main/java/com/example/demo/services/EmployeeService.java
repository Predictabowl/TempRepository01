package com.example.demo.services;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.Employee;

@Service
public class EmployeeService {

	private Map<Long, Employee> employees = new LinkedHashMap<>();
	
	public EmployeeService() {
		employees.put(1L, new Employee(1L, "John Doe", 1000));
		employees.put(2L, new Employee(2L, "John Smith", 2000));
	}

	public List<Employee> getAllEmployees() {
		return new LinkedList<>(employees.values());
	}

	public Employee getEmployeeById(long id) {
		return employees.get(id);		
	}

	public Employee insertNewEmployee(Employee employee) {
		employee.setId(employees.size()+1L);
		employees.put(employee.getId(), employee);
		return employee;
	}

	public Employee updateEmployeeById(long id, Employee employee) {
		employee.setId(id);
		employees.put(id, employee);
		return employee;
	}

}
