package com.example.demo.controllers;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Employee;
import com.example.demo.services.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = EmployeeRestController.class)
@ExtendWith(SpringExtension.class)
class EmployeeRestControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Test
	void testAllEmployeesEmpty() throws Exception {
		this.mvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}
	
	@Test
	void testAllEmployees_notEmpty() throws Exception {
		when(employeeService.getAllEmployees())
			.thenReturn(asList(
					new Employee(1L, "first", 1000),
					new Employee(2L, "second", 3000)));
		
		mvc.perform(get("/api/employees").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id",is(1)))
			.andExpect(jsonPath("$[0].name",is("first")))
			.andExpect(jsonPath("$[0].salary",is(1000)))
			.andExpect(jsonPath("$[1].id",is(2)))
			.andExpect(jsonPath("$[1].name",is("second")))
			.andExpect(jsonPath("$[1].salary",is(3000)));
	}
	
	@Test
	void test_oneEmployeeById_withExistingEmployee() throws Exception {
		when(employeeService.getEmployeeById(anyLong()))
			.thenReturn(new Employee(1L, "first",1000));
		
		mvc.perform(get("/api/employees/1").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.salary", is(1000)))
			.andExpect(jsonPath("$.name", is("first")));
	}
	
	@Test
	void test_oneEmployeeById_withoutExistingEmployee() throws Exception {
		when(employeeService.getEmployeeById(anyLong()))
			.thenReturn(null);
		
		mvc.perform(get("/api/employees/1").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
	
	@Test
	void test_postEmployee() throws JsonProcessingException, Exception {
		Employee newEmployee = new Employee(null, "new", 1100);
		ObjectMapper objectMapper = new ObjectMapper();
		
		when(employeeService.insertNewEmployee(newEmployee))
			.thenReturn(new Employee(1L, "new", 1100));
		
		mvc.perform(post("/api/employees/new")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newEmployee)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("new")))
			.andExpect(jsonPath("$.salary", is(1100)));
	}
	
	@Test
	void test_updateEmployee() throws JsonProcessingException, Exception {
		Employee requestBody = new Employee(null, "updated", 1200);
		ObjectMapper objectMapper = new ObjectMapper();
		
		when(employeeService.updateEmployeeById(2L, requestBody))
			.thenReturn(new Employee(2L, "updated", 1200));
		
		mvc.perform(put("/api/employees/update/2")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestBody)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(2)))
			.andExpect(jsonPath("$.name", is("updated")))
			.andExpect(jsonPath("$.salary", is(1200)));
	}
}
