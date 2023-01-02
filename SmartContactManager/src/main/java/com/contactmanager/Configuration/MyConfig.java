package com.contactmanager.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyConfig {

	@Bean
	public UserDetailsService getUserDetailsService() {
		
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	} 
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        
		 
		 http.authorizeHttpRequests(auth -> {auth.requestMatchers("/admin/").hasRole("ADMIN");
				auth.requestMatchers("/user/").hasRole("USER");
				auth.requestMatchers("/**").permitAll();
				
				})
			.formLogin().loginPage("/signin")
			.loginProcessingUrl("/post-signin").defaultSuccessUrl("/user/index")
			.and().csrf().disable();
		 
		 http.authenticationProvider(authenticationProvider());
		 
	        return http.build();
	    }

	/*
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        
		 
		 http.authorizeHttpRequests().requestMatchers("/admin/").hasRole("ADMIN")
			.requestMatchers("/user/").hasRole("USER")
			.requestMatchers("/**").permitAll().and().formLogin().loginPage("/signin")
			.loginProcessingUrl("/post-signin").defaultSuccessUrl("/user/index")
			.and().csrf().disable();
		 
		 http.authenticationProvider(authenticationProvider());
		 
	        return http.build();
	    }
	    
	    */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
			
		return authenticationConfiguration.getAuthenticationManager();
		
	}	

}
