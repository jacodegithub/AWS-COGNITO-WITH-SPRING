package com.kb.demo;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.nimbusds.jose.util.StandardCharset;

/*public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {

	private final String logoutUrl;
	private final String logoutRedirectUrl;
	private final String clientId;
	
	public CustomLogoutHandler(String logoutUrl, String logoutRedirectUrl, String clientId) {
		super();
		this.logoutUrl = logoutUrl;
		this.logoutRedirectUrl = logoutRedirectUrl;
		this.clientId = clientId;
	}
	
	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		return UriComponentsBuilder
				.fromUri(URI.create(logoutUrl))
				.queryParam("client_id", clientId)
				.queryParam("logout_uri", logoutRedirectUrl)
				.encode(StandardCharset.UTF_8)
				.build()
				.toUriString();
	}
}
*/


public class CustomLogoutHandler implements LogoutSuccessHandler {

	private final String logoutUrl;
	private final String logoutRedirectUrl;
	private final String clientId;
	
	public CustomLogoutHandler(String logoutUrl, String logoutRedirectUrl, String clientId) {
		super();
		this.logoutUrl = logoutUrl;
		this.logoutRedirectUrl = logoutRedirectUrl;
		this.clientId = clientId;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		response.sendRedirect(logoutUrl+"?client_id="+clientId+"&redirect_uri=http://localhost:8080/login/oauth2/code/cognito&response_type=code");
	}
}