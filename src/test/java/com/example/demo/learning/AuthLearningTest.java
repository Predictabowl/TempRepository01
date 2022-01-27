package com.example.demo.learning;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.config.Profiles;
import com.example.demo.controllers.EmployeeRestController;
import com.example.demo.controllers.EmployeeWebController;
import com.example.demo.services.EmployeeService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {EmployeeWebController.class, EmployeeRestController.class})
@ActiveProfiles(Profiles.TEST)
class AuthLearningTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	@Test
	@WithMockUser
	void test_web() throws Exception {
		mvc.perform(get("/"))
			.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	@WithMockUser(username = "mario")
	void test_rest() throws Exception {
		mvc.perform(get("/api/employees")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json("[]"));
	}
}
