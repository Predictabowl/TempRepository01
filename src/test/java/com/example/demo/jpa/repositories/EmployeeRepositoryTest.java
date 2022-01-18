package com.example.demo.jpa.repositories;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.Employee;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class EmployeeRepositoryTest {
	
	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void firstLearningTest() {
		Employee employee = new Employee(null, "test", 1000);
		Employee saved = repository.save(employee);
		Collection<Employee> employees = repository.findAll();
		
		assertThat(employees).containsExactly(saved);
	}
	
	@Test
	void secondLearningTest() {
		Employee employee = new Employee(null, "test", 1000);
		Employee saved = entityManager.persistFlushFind(employee);
		Collection<Employee> employees = repository.findAll();
		
		assertThat(employees).containsExactly(saved);
	}

	@Test
	void test_findByEmployeeName() {
		Employee saved = entityManager.persistFlushFind(new Employee(null, "test", 1050));
		Employee found = repository.findByName("test");
		
		assertThat(found).isEqualTo(saved);
	}
	
	@Test
	void test_findByNameAndSalary() {
		entityManager.persistFlushFind(new Employee(null, "test", 1000));
		Employee saved = entityManager.persistFlushFind(new Employee(null, "test", 2000));
		
		Collection<Employee> employees = repository.findByNameAndSalary("test", 2000);
		
		assertThat(employees).containsExactly(saved);
	}
	
	@Test
	void test_findByNameOrSalary() {
		Employee first = entityManager.persistFlushFind(new Employee(null, "test", 1000));
		Employee saved = entityManager.persistFlushFind(new Employee(null, "another", 2000));
		
		Collection<Employee> employees = repository.findByNameOrSalary("test", 2000);
		
		assertThat(employees).containsExactly(first,saved);
	}
	
	@Test
	void test_findAllEmployeesWithLowSalary() {
		Employee e1 = entityManager.persistFlushFind(new Employee(null, "first", 1000));
		Employee e2 = entityManager.persistFlushFind(new Employee(null, "second", 1500));
		entityManager.persistFlushFind(new Employee(null, "third", 3000));
		
		List<Employee> employees = repository.findAllWithLowSalary(2000);
		
		assertThat(employees).containsExactly(e1,e2);
	}
	
}
