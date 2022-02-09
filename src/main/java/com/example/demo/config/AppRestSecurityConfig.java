package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@Order(value = 1)
@Profile({Profiles.DEVELOPMENT, Profiles.PRODUCTION})
public class AppRestSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.antMatcher("/api/**")
			// send csrf token back as a cookie for a REST GET 
			.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.and()
//			.csrf().disable()
//			.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
//			.authorizeRequests().anyRequest().permitAll();
//			.authorizeRequests().regexMatchers(HttpMethod.GET, ".*").permitAll()
//		.and()
			.authorizeRequests().anyRequest().authenticated()
		.and()
			.httpBasic();
	}
	
}
