package com.kb.demo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private final CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;
	
	@Value("${aws.cognito.logoutUrl}")
	private String logoutUrl;
	
	@Value("${aws.cognito.logout.success.redirectUrl}")
	private String logoutRedirectUrl;
	
	@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	private String clientId;
	
	@Autowired
	public SecurityConfiguration(CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler) {
		this.customizeAuthenticationSuccessHandler = customizeAuthenticationSuccessHandler;
	}
	
	@Autowired
	private AwsCognitoJwtAuthFilter awsCognitoJwtAuthenticationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.headers().cacheControl();
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .antMatchers("/permit").permitAll()
                        .antMatchers("/login").permitAll()
                        .antMatchers("/user/greetMe").hasAnyRole("ADMIN", "USER")
                        .antMatchers("/admin/register-user").hasAnyRole("ADMIN")
                        .antMatchers("/api/**").authenticated())
                .addFilterBefore(awsCognitoJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth.redirectionEndpoint(endPoint -> endPoint.baseUri("/login/oauth2/code/cognito"))
        				.userInfoEndpoint(userInfoEndPointConfig -> userInfoEndPointConfig.userAuthoritiesMapper(userAuthoritiesMapper()))
        				.successHandler(customizeAuthenticationSuccessHandler))
        		.logout(logout -> {
        			logout.logoutSuccessHandler(new CustomLogoutHandler(logoutUrl, logoutRedirectUrl, clientId));
        		});
	}
	
	
	@Bean
	public GrantedAuthoritiesMapper userAuthoritiesMapper() {
		return authorities -> {
			Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
			
			if (!authorities.isEmpty() && authorities.iterator().next() instanceof OidcUserAuthority) {
	            OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authorities.iterator().next();

	            if (oidcUserAuthority.getAttributes().containsKey("cognito:groups")) {
	                mappedAuthorities = ((ArrayList<?>) oidcUserAuthority.getAttributes().get("cognito:groups")).stream()
	                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
	                        .collect(Collectors.toSet());
	            }
	        }
			
			return mappedAuthorities;
		};
	}

}