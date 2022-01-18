package com.example.demo.learning;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.Employee;
import com.example.demo.model.dto.EmployeeDTO;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ModelMapper.class)
class ModelMapperLearningTest {

	@Autowired
	private ModelMapper mapper;
	
	@Test
	void test_mapper() {
		Employee employee = new Employee(1L, "test", 1250);
		EmployeeDTO dto = mapper.map(employee, EmployeeDTO.class);
		
		assertThat(dto.getId()).isEqualTo(employee.getId());
		assertThat(dto.getName()).isEqualTo(employee.getName());
		assertThat(dto.getSalary()).isEqualTo(employee.getSalary());
	}
}
