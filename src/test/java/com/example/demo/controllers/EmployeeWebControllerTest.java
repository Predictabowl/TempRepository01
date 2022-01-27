package com.example.demo.controllers;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Employee;
import com.example.demo.model.dto.EmployeeDTO;
import com.example.demo.services.EmployeeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeWebController.class)
@AutoConfigureMockMvc(addFilters = false)
class EmployeeWebControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Test
	void testStatus200() throws Exception {
		mvc.perform(get("/"))
			.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	void testReturnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/")).andReturn().getModelAndView()
				, "index");
	}
	
	@Test
	void test_homeView_showsEmployees() throws Exception {
		List<Employee> employees = Arrays.asList(new Employee(1L, "test", 1200));
		
		when(employeeService.getAllEmployees())
			.thenReturn(employees);
		
		mvc.perform(get("/"))
			.andExpect(view().name("index"))
			.andExpect(model().attribute("employees", employees))
			.andExpect(model().attribute("message", ""));
	}
	
	@Test
	void test_homeView_showsEmployees_whenThereAreNoEmployees() throws Exception {
		when(employeeService.getAllEmployees())
			.thenReturn(Collections.emptyList());
		
		mvc.perform(get("/"))
			.andExpect(view().name("index"))
			.andExpect(model().attribute("employees", Collections.emptyList()))
			.andExpect(model().attribute("message", "No employee"));
	}
	
	@Test
	void test_EditEmployee_whenEmployeeIsFound() throws Exception {
		Employee employee = new Employee(1L, "test", 1000);
		
		when(employeeService.getEmployeeById(1L))
			.thenReturn(employee);
		
		mvc.perform(get("/edit/1"))
			.andExpect(view().name("edit"))
			.andExpect(model().attribute("employee", employee))
			.andExpect(model().attribute("message", ""));
	}
	
	@Test
	void test_EditEmployee_whenEmployeeIsNotFound() throws Exception {
		when(employeeService.getEmployeeById(1L))
			.thenReturn(null);
		
		mvc.perform(get("/edit/1"))
			.andExpect(view().name("edit"))
			.andExpect(model().attribute("employee", nullValue()))
			.andExpect(model().attribute("message", "No employee found with id: 1"));
	}
	
	@Test
	void test_EditNewEmployee() throws Exception {
		mvc.perform(get("/new"))
			.andExpect(view().name("edit"))
			.andExpect(model().attribute("employee", new Employee()))
			.andExpect(model().attribute("message", ""));
		
		verifyNoInteractions(employeeService);
	}
	
	@Test
	void test_PostEmployeeWithoutId_shouldInsertNewEmployee() throws Exception {
		mvc.perform(post("/save")
				.param("name", "test name")
				.param("salary", "1000"))
			.andExpect(view().name("redirect:/"));
		
		verify(employeeService).insertNewEmployee(new EmployeeDTO(null,"test name",1000));
	}

	@Test
	void test_PostEmployeeWithId_shoulUpdateExistingEmployee() throws Exception {
		mvc.perform(post("/save")
				.param("id", "1")
				.param("name", "edited")
				.param("salary", "1250"))
			.andExpect(view().name("redirect:/"));
		
		verify(employeeService).updateEmployeeById(1L, new EmployeeDTO(1L,"edited",1250));
	}
	
	@Test
	void test_deleteEmployeeWithId_shouldDeleteExistingEmployee() throws Exception {
		mvc.perform(get("/delete/2"))
			.andExpect(view().name("redirect:/"));
		
		verify(employeeService).deleteEmployeeById(2L);
	}
}
