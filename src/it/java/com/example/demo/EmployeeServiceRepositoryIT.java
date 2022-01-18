package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.jpa.repositories.EmployeeRepository;
import com.example.demo.model.Employee;
import com.example.demo.services.EmployeeService;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(EmployeeService.class)
class EmployeeServiceRepositoryIT {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Test
	void test_serviceCanInsertIntoRepository() {
		Employee saved = employeeService.insertNewEmployee(new Employee(null, "an employee", 1200));

		assertThat(employeeRepository.findById(saved.getId())).isPresent();
	}

	@Test
	void test_serviceCanUpdateIntoRepository() {
		Employee saved = employeeRepository.save(new Employee(null, "an employee", 1200));
		Employee modified = employeeService.updateEmployeeById(saved.getId(),
				new Employee(saved.getId(), "modified", 2000));
		
		assertThat(employeeRepository.findById(saved.getId()))
			.contains(modified);
	}
}
