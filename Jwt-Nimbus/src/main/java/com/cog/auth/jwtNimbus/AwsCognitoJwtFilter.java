package com.cog.auth.jwtNimbus;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AwsCognitoJwtFilter extends GenericFilter {
	
	private static final Logger log = LoggerFactory.getLogger(AwsCognitoJwtFilter.class);
	
	@Autowired
	private AwsCognitoTokenProcessor awsCognitoTokenProcessor;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		Authentication  authentication;
		
		try {
			authentication = this.awsCognitoTokenProcessor.authenticate((HttpServletRequest) request);
			
			if(authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		catch(Exception ex) {
			log.error("Cognito Id Token processing error!",ex);
			SecurityContextHolder.clearContext();
		}
		
		
		chain.doFilter(request, response);
	}

}
