package com.aws.cognito.security;

import java.net.URI;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.nimbusds.jose.util.StandardCharset;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {

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
