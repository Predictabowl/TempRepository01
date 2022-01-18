package com.example.demo.services;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.demo.jpa.repositories.EmployeeRepository;
import com.example.demo.model.Employee;
import com.example.demo.model.dto.EmployeeDTO;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceWithMockitoTest {

	@Mock
	private EmployeeRepository employeeRepository;

	private EmployeeService employeeService;	
	private ModelMapper mapper;
	
	@BeforeEach
	void setUp() {
		mapper = new ModelMapper();
		employeeService = new EmployeeService(employeeRepository, mapper);
	}

	@Test
	void test_getAllEmployees() {
		Employee employee1 = new Employee(1L, "first", 1000);
		Employee employee2 = new Employee(2L, "second", 5000);

		when(employeeRepository.findAll()).thenReturn(asList(employee1, employee2));

		assertThat(employeeService.getAllEmployees()).containsExactly(employee1, employee2);
	}

	@Test
	void test_getEmployeeById_found() {
		Employee employee = new Employee(1L, "test", 1000);

		when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

		assertThat(employeeService.getEmployeeById(1)).isSameAs(employee);
	}

	@Test
	void test_getEmployeeById_notFound() {
		when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThat(employeeService.getEmployeeById(1L)).isNull();
	}

	@Test
	void test_insertNewEmployee_setsIdToNull_and_returnSavedEmployee() {
		EmployeeDTO toSave = spy(new EmployeeDTO(99L, "", 0));
		Employee saved = new Employee(1L, "saved", 1000);
		
		when(employeeRepository.save(isA(Employee.class))).thenReturn(saved);
		
		Employee result = employeeService.insertNewEmployee(toSave);
		
		assertThat(result).isSameAs(saved);
		
		InOrder inOrder = inOrder(toSave,employeeRepository);
		inOrder.verify(toSave).setId(null);
		inOrder.verify(employeeRepository).save(new Employee(null, "", 0));
	}
	
	@Test
	void test_updateEmployeeById_setsIdToArgument_and_returnsAvedEmployee() {
		EmployeeDTO replacement = spy(new EmployeeDTO(null, "employee", 0));
		Employee replaced = new Employee(1L, "saved", 1000);
		
		when(employeeRepository.save(isA(Employee.class))).thenReturn(replaced);
		
		Employee result = employeeService.updateEmployeeById(1L, replacement);
		
		assertThat(result).isSameAs(replaced);
		
		InOrder inOrder = inOrder(replacement,employeeRepository);
		inOrder.verify(replacement).setId(1L);
		inOrder.verify(employeeRepository).save(new Employee(1L, "employee", 0));
	}
}
