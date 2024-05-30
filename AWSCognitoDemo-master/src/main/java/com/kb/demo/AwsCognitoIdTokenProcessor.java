package com.kb.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static java.util.List.of;

import java.text.ParseException;

@Component
public class AwsCognitoIdTokenProcessor {

//	spring:
//		  security:
//		    oauth2:
//		      client:
//		        registration:
//		          cognito:
//		            client-id: 1iadgmvsjtvnh553bk283mga5e
	
	@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	private String cognitoUserPoolId;
	
    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private ConfigurableJWTProcessor configurableJWTProcessor;
    
    private String jwtToken;
    

    public Authentication authenticate(HttpServletRequest request) throws Exception {
        String idToken = request.getHeader(this.jwtConfiguration.getHttpHeader());
		/* String idToken = jwtToken; */
        if (idToken != null) {
            JWTClaimsSet claims = this.configurableJWTProcessor.process(this.getBearerToken(idToken),null);
            validateIssuer(claims);
            verifyIfIdToken(claims);
            String username = getUserNameFrom(claims);
            if (username != null) {
            	
                List<GrantedAuthority> grantedAuthorities = Arrays.asList( new SimpleGrantedAuthority("ROLE_ADMIN"));
                User user = new User(username, "", Arrays.asList());
                return new JwtAuthentication(user, claims, grantedAuthorities);
            }
        }
        return null;
    }
    
    public void getIdToken(String email, String password) {
        //AuthenticationHelper authenticationHelper = new AuthenticationHelper(cognitoUserPool, cognitoUserPool.getClientId());
        AWSCognitoIdentityProvider cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(Regions.US_EAST_1) // Specify your AWS region
                .build();
        
        Map<String, String> authParameters = new HashMap<>();
        authParameters.put("EMAIL", email);
        authParameters.put("PASSWORD", password);
        
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withClientId(cognitoUserPoolId)
                .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .withAuthParameters(authParameters);
        InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);
        jwtToken = authResult.getAuthenticationResult().getIdToken();
    }

    private String getUserNameFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(this.jwtConfiguration.getUserNameField()).toString();
    }

    private void verifyIfIdToken(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception("JWT Token is not an ID Token");
        }
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(this.jwtConfiguration.getCognitoIdentityPoolUrl())) {
            throw new Exception(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(), this.jwtConfiguration.getCognitoIdentityPoolUrl()));
        }
    }

    private String getBearerToken(String token) {
        return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
    }
}
