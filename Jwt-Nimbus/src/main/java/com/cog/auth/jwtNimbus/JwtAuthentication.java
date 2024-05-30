package com.cog.auth.jwtNimbus;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.nimbusds.jwt.JWTClaimsSet;

public class JwtAuthentication extends AbstractAuthenticationToken {

	private final Object principal;
	private JWTClaimsSet jwtClaims;
	
	public JwtAuthentication(Object principal, JWTClaimsSet jwtClaims, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.jwtClaims = jwtClaims;
		this.principal = principal;
		super.setAuthenticated(true);
	}

	public Object getCredentials() {
		return null;
	}
	
	public Object getPrincipal() {
		return this.principal;
	}
	
	public JWTClaimsSet getJwtClaimsSet() {
		return this.jwtClaims;
	}
}
