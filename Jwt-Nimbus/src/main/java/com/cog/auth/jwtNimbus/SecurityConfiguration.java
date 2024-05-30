package com.cog.auth.jwtNimbus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private AwsCognitoJwtFilter awsFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> 
		
			auth.requestMatchers("/").permitAll()
			.anyRequest().authenticated()
				)
		.addFilterBefore(awsFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
