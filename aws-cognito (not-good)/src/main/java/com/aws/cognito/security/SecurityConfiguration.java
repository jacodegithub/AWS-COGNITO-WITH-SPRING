package com.aws.cognito.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
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
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
	
		http.cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()));
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> 
		{
			auth.antMatchers("/").permitAll();
			auth.antMatchers(HttpMethod.POST, "/admin/register-user").hasRole("ADMIN");
			auth.antMatchers("admin/*").hasAnyRole("ADMIN");
			auth.antMatchers("user/*").hasAnyRole("ADMIN", "USER").anyRequest().authenticated();
			
		})
		
		.oauth2Login(oauth -> oauth.redirectionEndpoint(endPoint -> endPoint.baseUri("/login/oauth2/code/cognito"))
				.userInfoEndpoint(userInfoEndPointConfig -> userInfoEndPointConfig.userAuthoritiesMapper(userAuthoritiesMapper()))
				.successHandler(customizeAuthenticationSuccessHandler))
		.logout(logout -> {
			logout.logoutSuccessHandler(new CustomLogoutHandler(logoutUrl, logoutRedirectUrl, clientId));
		});
		
		return http.build();
	}
	
	
	  @Bean
	    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
	        StrictHttpFirewall firewall = new StrictHttpFirewall();
	        firewall.setAllowSemicolon(true); // Allow semicolon in URL
	        return firewall;
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
	
	
//	@Bean
//	public CorsFilter corsFilter() {
//		 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	        CorsConfiguration config = new CorsConfiguration();
//	        config.setAllowCredentials(true); // you might need this if your frontend and backend run on different origins
//	        config.addAllowedHeader("*");
//	        config.addAllowedMethod("*");
//	        source.registerCorsConfiguration("/**", config);
//	        return new CorsFilter(source);
//	}
	
//    @Bean
//    public FilterRegistrationBean<CorsFilter> coresFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        
//        List<String> methods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS");
//        
//        
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5500"));
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.setMaxAge(3600L);
//        
//        source.registerCorsConfiguration("/**", corsConfiguration);
//
//        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
//        bean.addUrlPatterns("/*");
//        
//        bean.setOrder(-1);
//
//        return bean;
//    }

}
