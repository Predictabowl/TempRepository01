package com.example.demo.services;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmployeesConfig {
	
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
