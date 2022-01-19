package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.github.bonigarcia.wdm.WebDriverManager;

class EmployeeWebControllerE2E {

	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
	private static String baseUrl = "http://localhost:" + port;
	private WebDriver webDriver;
	
	private static final Logger LOGGER = LogManager.getLogger(EmployeeWebControllerE2E.class);

	@BeforeAll
	static void setUpClass() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	void setUp() {
		webDriver = new ChromeDriver();
	}

	@AfterEach
	void tearDown() {
		webDriver.quit();
	}

	@Test
	void test_createNewEmployee() {
		webDriver.get(baseUrl);
		webDriver.findElement(By.cssSelector("a[href*='/new")).click();

		webDriver.findElement(By.name("name")).sendKeys("new employee");
		webDriver.findElement(By.name("salary")).sendKeys("2000");
		webDriver.findElement(By.name("btn_submit")).click();

		assertThat(webDriver.findElement(By.id("employee_table")).getText()).contains("new employee", "2000");
	}
	
	@Test
	void test_editEmployee() throws JSONException {
		String id = postEmployee("employee to edit", 3000);
		webDriver.get(baseUrl);
		webDriver.findElement(By.cssSelector("a[href*='/edit/"+id+"']")).click();
		WebElement nameElement = webDriver.findElement(By.name("name"));
		nameElement.clear();
		nameElement.sendKeys("edited employee");
		WebElement salaryField = webDriver.findElement(By.name("salary"));
		salaryField.clear();
		salaryField.sendKeys("2000");
		webDriver.findElement(By.name("btn_submit")).click();
		
		assertThat(webDriver.findElement(By.id("employee_table")).getText())
			.contains("edited employee","2000")
			.doesNotContain("employee to edit","3000");
	}
	
	@Test
	void test_deleteEmployee() throws JSONException {
		String id = postEmployee("employee to delete", 2500);
		
		webDriver.get(baseUrl);
		webDriver.findElement(By.cssSelector("a[href*='/delete/"+id+"']")).click();
		
		assertThat(webDriver.findElement(By.tagName("body")).getText())
			.doesNotContain("employee to delete");
		
	}

	private String postEmployee(String name, int salary) throws JSONException {
		JSONObject body = new JSONObject();
		body.put("name", name);
		body.put("salary", salary);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> answer = restTemplate.postForEntity(baseUrl + "/api/employees/new", entity,
				String.class);
		LOGGER.debug("answer for rest POST: "+answer);

		return new JSONObject(answer.getBody()).get("id").toString();
	}
}
