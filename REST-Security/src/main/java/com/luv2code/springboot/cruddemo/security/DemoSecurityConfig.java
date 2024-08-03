package com.luv2code.springboot.cruddemo.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class DemoSecurityConfig {
	
	
	 //when using customize scheme and jdbcAutehntication 
	
	@Bean
	public UserDetailsManager userdetailsManager(DataSource dataSource)
	{
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		
		//define query to retrieve user and role by username
		
		jdbcUserDetailsManager.setUsersByUsernameQuery("select user_id,pw,active from members where user_id=?");
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select user_id,role from roles where user_id=?");
		
		return jdbcUserDetailsManager;
	}
	
	
	
	
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(configurer ->
		configurer 
		
		 .requestMatchers(HttpMethod.GET,"/api/employees").hasRole("EMPLOYEE")
		 .requestMatchers(HttpMethod.GET,"/api/employees/**").hasRole("EMPLOYEE")
		 .requestMatchers(HttpMethod.POST,"/api/employees").hasRole("MANAGER")
		 .requestMatchers(HttpMethod.PUT,"/api/employees").hasRole("MANAGER")
		 .requestMatchers(HttpMethod.DELETE,"/api/employees/**").hasRole("ADMIN")
		 
		);
		
		//use Basic HTTP Authentication
		
		http.httpBasic(Customizer.withDefaults());
		
		//disable csrf
		
		http.csrf(csrf ->csrf.disable());
		
		return http.build();
		
		
			
		
		
		
		// add support for JDBC Authentication
		
		/*
		@Bean
		public UserDetailsManager userdetailsManager(DataSource dataSource)
		{
			return new JdbcUserDetailsManager(dataSource);
		}
		
		*/
		
		
		
		
		/* 
		@Bean
		public InMemoryUserDetailsManager userdetailsManager() {
			
			UserDetails john =User.builder()
					.username("jhon")
					.password("{noop}test123")
					.roles("EMPLOYEE")
					.build();
			
			UserDetails mary =User.builder()
					.username("mary")
					.password("{noop}test123")
					.roles("EMPLOYEE","MANAGER")
					.build();
			
			UserDetails susan =User.builder()
					.username("susan")
					.password("{noop}test123")
					.roles("EMPLOYEE","MANAGER","ADMIN")
					.build();
			return new InMemoryUserDetailsManager(john,mary,susan);
		}
		
		*/
		
	}	
	
}
