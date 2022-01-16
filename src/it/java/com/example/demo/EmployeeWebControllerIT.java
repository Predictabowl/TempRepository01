package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Driver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.jpa.repositories.EmployeeRepository;
import com.example.demo.model.Employee;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class EmployeeWebControllerIT {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@LocalServerPort
	private int port;
	
	private WebDriver webDriver;
	private String baseUrl;
	
	@BeforeEach
	void setUp() {
		baseUrl = "http://localhost:"+port;
		webDriver = new HtmlUnitDriver();
		employeeRepository.deleteAll();
		employeeRepository.flush();
	}
	
	@AfterEach
	void tearDown() {
		webDriver.quit();
	}
	
	@Test
	void test_homePage() {
		Employee testEmployee = employeeRepository.save(new Employee(null, "test", 1000));
		
		webDriver.get(baseUrl);
		
		assertThat(webDriver.findElement(By.id("employee_table")).getText())
			.contains("test","1000","Edit");
		
		webDriver.findElement(By.cssSelector("a[href*='/edit/"+testEmployee.getId()+"']"));
	}
	
	@Test
	void test_editPageNMewEmployee() {
		webDriver.get(baseUrl+"/new");
		
		webDriver.findElement(By.name("name")).sendKeys("new employee");
		webDriver.findElement(By.name("salary")).sendKeys("2000");
		webDriver.findElement(By.name("btn_submit")).click();
		
		assertThat(employeeRepository.findByName("new employee").getSalary())
			.isEqualTo(2000);
	}
	
	@Test
	void test_editPageUpdateEmployee() {
		Employee testEmployee = employeeRepository.save(new Employee(null, "test employee", 1000));
		
		webDriver.get(baseUrl+"/edit/"+testEmployee.getId());
		
		WebElement nameField = webDriver.findElement(By.name("name"));
		WebElement salaryField = webDriver.findElement(By.name("salary"));
		nameField.clear();
		nameField.sendKeys("modified employee");
		salaryField.clear();
		salaryField.sendKeys("1500");
		webDriver.findElement(By.name("btn_submit")).click();
		
		assertThat(employeeRepository.findByName("modified employee").getSalary())
			.isEqualTo(1500);
	}
}
