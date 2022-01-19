package com.example.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.model.Employee;
import com.example.demo.model.dto.EmployeeDTO;
import com.example.demo.services.EmployeeService;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeWebController.class)
class EmployeeWebControllerHtmlUnitTest {

	@Autowired
	private WebClient webClient;

	@MockBean
	private EmployeeService employeeService;

	@Test
	void test_homePageTitle() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = webClient.getPage("/");

		assertThat(page.getTitleText()).isEqualTo("Employees");
	}

	@Test
	void test_homePage_withNoEmployees() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

		HtmlPage page = webClient.getPage("/");

		assertThat(page.getBody().getTextContent()).contains("No employee");
	}

	@Test
	void test_homePage_withEmployees_shouldShowThemInTable() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(employeeService.getAllEmployees())
				.thenReturn(Arrays.asList(new Employee(1L, "first", 1000), new Employee(2L, "second", 2000)));
		
		HtmlPage page = webClient.getPage("/");
		HtmlTable table = page.getHtmlElementById("employee_table");
		
		assertThat(page.getBody().getTextContent())
			.doesNotContain("No employee");
		assertThat(table.asNormalizedText())
			.contains("ID	Name	Salary")
			.contains("1	first	1000	Edit	Delete")
			.contains("2	second	2000	Edit	Delete");
		page.getAnchorByHref("/edit/1");
		page.getAnchorByHref("/edit/2");
		page.getAnchorByHref("/delete/1");
		page.getAnchorByHref("/delete/2");
	}
	
	@Test
	void test_editPageTitle() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = webClient.getPage("/edit/1");
		
		assertThat(page.getTitleText()).isEqualTo("Employees");
	}
	
	@Test
	void test_editPage_nonExistentEmployee() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(employeeService.getEmployeeById(anyLong()))
			.thenReturn(null);
		
		HtmlPage page = webClient.getPage("/edit/3");
		
		assertThat(page.getBody().getTextContent()).contains("No employee found with id: 3");
	}
	
	@Test
	void test_EditExistentEmployee() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(employeeService.getEmployeeById(2))
			.thenReturn(new Employee(2L, "original name", 1000));
		
		HtmlPage page = webClient.getPage("/edit/2");
		
		final HtmlForm form = page.getFormByName("employee_form");
		
		form.getInputByValue("original name").setValueAttribute("modified name");
		form.getInputByValue("1000").setValueAttribute("2000");
		form.getButtonByName("btn_submit").click();
		
		verify(employeeService).updateEmployeeById(2, new EmployeeDTO(2L, "modified name", 2000));
	}
	
	@Test
	void test_editNewEmployee() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = webClient.getPage("/new");
		
		final HtmlForm form = page.getFormByName("employee_form");
		form.getInputByName("name").setValueAttribute("new name");
		form.getInputByName("salary").setValueAttribute("1300");
		form.getButtonByName("btn_submit").click();
		
		verify(employeeService).insertNewEmployee(new EmployeeDTO(null, "new name", 1300));
	}
	
	@Test
	void test_homePage_shouldProvideLinkForCreatingNewEmployee() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = webClient.getPage("/");
		
		assertThat(page.getAnchorByText("New employee").getHrefAttribute())
			.isEqualTo("/new");
	}
	
}