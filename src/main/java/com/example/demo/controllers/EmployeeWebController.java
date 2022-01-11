package com.example.demo.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Employee;
import com.example.demo.services.EmployeeService;

@Controller
public class EmployeeWebController {
	
	private static final String MESSAGE_ATTRIBUTE = "message";
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/")
	public String index(Model model) {
		List<Employee> allEmployees = employeeService.getAllEmployees();
		model.addAttribute("employees",allEmployees);
		model.addAttribute(MESSAGE_ATTRIBUTE,allEmployees.isEmpty() ? "No employee" : "");
		return "index";
	}
	
	@GetMapping("/edit/{id}")
	public String editEmployee(@PathVariable long id, Model model) {
		Employee employee = employeeService.getEmployeeById(id);
		model.addAttribute("employee",employee);
		model.addAttribute(MESSAGE_ATTRIBUTE, Objects.isNull(employee) ? "No employee found with id: "+id : "");
		return "edit";
	}
	
	@GetMapping("/new")
	public String newEmployee(Model model) {
		model.addAttribute("employee",new Employee());
		model.addAttribute(MESSAGE_ATTRIBUTE,"");
		return "edit";
	}
	
	@PostMapping("/save")
	public String saveEmployee(Employee employee) {
		Long id = employee.getId();
		if (Objects.isNull(id))
			employeeService.insertNewEmployee(employee);
		else
			employeeService.updateEmployeeById(id, employee);
		return "redirect:/";
	}
}
