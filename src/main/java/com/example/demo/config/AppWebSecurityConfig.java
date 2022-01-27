package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Profile({Profiles.DEVELOPMENT, Profiles.PRODUCTION})
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
//			.csrf().disable()
			.authorizeRequests().anyRequest().authenticated()
			.and()
//			.formLogin();
			.formLogin(form -> form.loginPage("/login").permitAll());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/css/**","/webjars/**");
	}
	
}
