package com.cog.auth.jwtNimbus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties(
//		prefix = ""
//		)
public class JwtConfig {
	
	@Value("${cognito.userPoolId}")
	private String userPoolId;
	private String jwkUrl;
	private String region = "us-east-1";
	private String httpHeader = "Authurization";
	private int connectionTimeout = 3600;
	private int readTimeout = 3600;
	private String userNameField = "cognito:username";
	
	public JwtConfig() {}
	
	public String getJwkUrl() {
		return this.jwkUrl != null && !this.jwkUrl.isEmpty() ? this.jwkUrl : String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", this.region, this.userPoolId);
	}
	
	public String getUserPoolId() {
		return this.userPoolId;
	}
	
	public void setUserPoolId(String userPoolId) {
		this.userPoolId = userPoolId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getHttpHeader() {
		return httpHeader;
	}

	public void setHttpHeader(String httpHeader) {
		this.httpHeader = httpHeader;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getUserNameField() {
		return userNameField;
	}

	public void setUserNameField(String userNameField) {
		this.userNameField = userNameField;
	}

	public void setJwkUrl(String jwkUrl) {
		this.jwkUrl = jwkUrl;
	}
	
	
}
