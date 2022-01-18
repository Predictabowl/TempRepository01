package com.example.demo.controllers;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import com.example.demo.model.Employee;
import com.example.demo.model.dto.EmployeeDTO;
import com.example.demo.services.EmployeeService;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@ExtendWith(MockitoExtension.class)
class EmployeeRestControllerRestAssuredTest {

	@Mock
	private EmployeeService employeeService;
	
	@InjectMocks
	private EmployeeRestController employeeRestController;
	
	@BeforeEach
	void setup() {
		RestAssuredMockMvc.standaloneSetup(employeeRestController);
	}
	
	@Test
	void test_findByIdWithExistingEmployee() {
		when(employeeService.getEmployeeById(1))
			.thenReturn(new Employee(1L, "first", 1000));
		
		given()
		.when()
			.get("/api/employees/1")
		.then()
			.statusCode(200)
			.assertThat()
			.body(
				"id", equalTo(1),
				"name", equalTo("first"),
				"salary", equalTo(1000));
		
		verify(employeeService,times(1)).getEmployeeById(1);
	}
	
	@Test
	void test_postEmployee(){
		EmployeeDTO requestBodyEmployee = new EmployeeDTO(null, "test", 1000);
		
		when(employeeService.insertNewEmployee(requestBodyEmployee))
			.thenReturn(new Employee(1L, "test", 1000));
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(requestBodyEmployee)
		.when()
			.post("/api/employees/new")
		.then()
			.statusCode(200)
			.body(
				"id", equalTo(1),
				"name", equalTo("test"),
				"salary", equalTo(1000));
	}
	
	@Test
	void test_updateEmployee() {
		EmployeeDTO requestBodyEmployee = new EmployeeDTO(null, "test", 1200);
		
		when(employeeService.updateEmployeeById(1L, requestBodyEmployee))
			.thenReturn(new Employee(1L, "test", 1200));
		
		given()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(requestBodyEmployee)
		.when()
			.put("/api/employees/update/1")
		.then()
			.statusCode(200)
			.body(
					"id", equalTo(1),
					"name", equalTo("test"),
					"salary", equalTo(1200));
	}
}
