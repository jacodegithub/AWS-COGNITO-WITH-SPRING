package com.cog.auth.jwtNimbus;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import static com.nimbusds.jose.JWSAlgorithm.RS256;


@SpringBootApplication
@Configuration
public class JwtNimbusApplication {

	@Autowired
	private JwtConfig jwtConfiguration;
	
	public static void main(String[] args) {
		SpringApplication.run(JwtNimbusApplication.class, args);
	}
	
	@Bean	
	public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor() throws IOException, ParseException {
		
	    URL jwkSetURL= new URL(jwtConfiguration.getJwkUrl());
	    JWKSet downloadedJWKSet = JWKSet.load(jwkSetURL);
	    JWKSource<?> keySource= new ImmutableJWKSet<>(downloadedJWKSet);
	    ConfigurableJWTProcessor jwtProcessor= new DefaultJWTProcessor<>();
	    JWSKeySelector<?> keySelector= new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
	    jwtProcessor.setJWSKeySelector(keySelector);
	    return jwtProcessor;
	}

}
