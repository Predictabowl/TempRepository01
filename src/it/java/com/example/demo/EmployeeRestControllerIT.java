package com.example.demo;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.jpa.repositories.EmployeeRepository;
import com.example.demo.model.Employee;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.bytebuddy.NamingStrategy.SuffixingRandom.BaseNameResolver.ForGivenType;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EmployeeRestControllerIT {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@LocalServerPort
	private int port;
	
	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		employeeRepository.deleteAll();
		employeeRepository.flush();
	}
	
	@Test
	void test_newEmployee() {
		Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(new Employee(null, "new employee", 1000))
			.when()
				.post("/api/employees/new");
		
		Employee saved = response.getBody().as(Employee.class);
		
		assertThat(employeeRepository.findById(saved.getId()).get())
			.isEqualTo(saved);
	}
	
	@Test
	void test_updateEmployee() {
		Employee saved = employeeRepository.save(new Employee(null, "original", 1100));
		
		given().contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new Employee(null, "modified", 1200))
		.when()
			.put("/api/employees/update/"+saved.getId())
		.then()
			.statusCode(200)
			.body("id", equalTo(saved.getId().intValue()),
					"salary", equalTo(1200),
					"name", equalTo("modified"));
	}

}
