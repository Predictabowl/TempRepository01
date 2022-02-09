package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.jpa.repositories.UserRepository;
import com.example.demo.model.User;

@Configuration
@EnableWebSecurity
@Profile({Profiles.DEVELOPMENT, Profiles.PRODUCTION})
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserRepository userRepository;
	

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		userRepository.save(new User(null, "mario", getPasswordEncoder().encode("mario")));
		
		http
//			.csrf().disable()
				.authorizeRequests().anyRequest().authenticated()
			.and()
//				.formLogin();
				.formLogin(form -> form.loginPage("/login").permitAll())
			.logout();
//			.logout(logout -> logout
//					.logoutUrl("/logout")
//					.logoutSuccessUrl("/login?logout")
//					.invalidateHttpSession(true)
//					.deleteCookies("JSESSIONID"));
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/css/**","/webjars/**");
	}
	
}
