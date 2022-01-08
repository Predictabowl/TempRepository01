package com.example.demo.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static java.util.Arrays.asList;

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
}
