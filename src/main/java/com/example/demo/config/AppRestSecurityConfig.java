package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(value = 1)
@Profile({Profiles.DEVELOPMENT, Profiles.PRODUCTION})
public class AppRestSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/api/**")
			.csrf().disable()
//			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//			.authorizeRequests().anyRequest().permitAll();
			.authorizeRequests().anyRequest().authenticated()
			.and()
			.httpBasic();
	}
	
}
