package com.example.demo.jpa;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.Employee;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class EmployeeJpaTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void testJpaMapping() {
		Employee saved = entityManager.persistFlushFind(new Employee(null, "test", 1000));
		
		assertThat(saved.getName()).isEqualTo("test");
		assertThat(saved.getSalary()).isEqualTo(1000);
		assertThat(saved.getId()).isNotNull();
		assertThat(saved.getId()).isGreaterThan(0);
		
		LoggerFactory.getLogger(EmployeeJpaTest.class).info("Saved POJO: "+saved.toString());
	}
}
