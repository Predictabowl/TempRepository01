package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Employee;

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
		webLogin();
	}

	private void webLogin() {
		webDriver.get(baseUrl);
		webDriver.findElement(By.id("username")).sendKeys("admin");
		webDriver.findElement(By.id("password")).sendKeys("password");
		webDriver.findElement(By.tagName("button")).click();
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

	@Test
	void test_learning() throws JSONException {
		String id = postEmployee("employee to learn", 2500);
	}
	
	private Map<String,String> retrieveCookies() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth("admin", "password");
		HttpEntity<String> entity = new HttpEntity<>("", headers);
		
		LOGGER.debug("Entity :"+entity);
		ResponseEntity<String> answer = restTemplate.exchange(baseUrl + "/api/employees", HttpMethod.GET, entity, String.class); 
		LOGGER.debug("answer for rest GET: "+answer);
		List<String> cookies = answer.getHeaders().get("Set-Cookie");
		Map<String,String> collect = 
				Arrays.stream(cookies.get(0).split(";"))
			.map(s ->  s.split("=",2))
			.filter(s -> s[0].equals("XSRF-TOKEN"))
			.collect(Collectors.toMap(s -> s[0], s -> s[1]));
		LOGGER.debug("answer CSRF Token: "+collect.get("XSRF-TOKEN"));
		Map<String,String> collect2 = Arrays.stream(cookies.get(1).split(";"))
		.map(s ->  s.split("=",2))
		.filter(s -> s[0].equals("JSESSIONID"))
		.collect(Collectors.toMap(s -> s[0], s -> s[1]));
		
		collect.put("JSESSIONID", collect2.get("JSESSIONID"));
		LOGGER.debug("answer JSESSIONID: "+collect.get("JSESSIONID"));
		
		return collect;
	}
	
	private String postEmployee(String name, int salary) throws JSONException {
		RestTemplate restTemplate = new RestTemplate();
		Map<String,String> cookies = retrieveCookies();
		String csrfToken = cookies.get("XSRF-TOKEN");
		String sessionId = cookies.get("JSESSIONID");
		
		JSONObject body = new JSONObject();
		body.put("name", name);
		body.put("salary", salary);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth("admin", "password");
		headers.set("X-XSRF-TOKEN", csrfToken);
		headers.set("Cookie", "JSESSIONID="+sessionId+"; XSRF-TOKEN="+csrfToken);
		HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

		LOGGER.debug("Entity :"+entity);
		ResponseEntity<String> answer = restTemplate.postForEntity(baseUrl + "/api/employees/new", entity,
				String.class);
		LOGGER.debug("answer for rest POST: "+answer);

		return new JSONObject(answer.getBody()).get("id").toString();
	}
	
}
