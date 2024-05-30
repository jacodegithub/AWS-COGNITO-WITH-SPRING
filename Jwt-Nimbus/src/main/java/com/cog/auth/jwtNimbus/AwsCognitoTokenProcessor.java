package com.cog.auth.jwtNimbus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AwsCognitoTokenProcessor {

	@Autowired
	private JwtConfig jwtConfig;
	
	@Autowired
	private ConfigurableJWTProcessor<?> configurableJwtProcessor;
	
	
	public Authentication authenticate(HttpServletRequest request) throws Exception {
		
		String idToken = request.getHeader(this.jwtConfig.getHttpHeader());
		if(idToken != null) {
			JWTClaimsSet claims = this.configurableJwtProcessor.process(this.getBearerToken(idToken), null);
			validateIssuer(claims);
			String username = getUserNameFrom(claims);
			
			if(username != null) {
				List<GrantedAuthority> grantedAuth = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
				User user = new User(username, "", List.of()); 
				return new JwtAuthentication(user, claims, grantedAuth);
			}
		}
		
		return null;
	}
	
	private String getUserNameFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(this.jwtConfig.getUserNameField()).toString();
    }
	
	private void validateIssuer(JWTClaimsSet claims) throws Exception {
		if(!claims.getIssuer().equals(this.jwtConfig.getUserPoolId())) {
			throw new Exception("JWT Token is not an ID Token");
		}
	}
	
	private String getBearerToken(String token) {
		return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token; 
	}
}
