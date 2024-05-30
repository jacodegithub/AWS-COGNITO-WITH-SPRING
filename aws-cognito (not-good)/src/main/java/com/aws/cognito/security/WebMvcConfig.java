package com.aws.cognito.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//	 @Override
//	    public void addCorsMappings(CorsRegistry registry) {
//	        registry.addMapping("/**")
//	                .allowedOrigins("*") // Allow all origins
//	                .allowedMethods("*") // Allow all methods (GET, POST, etc.)
//	                .allowedHeaders("*"); // Allow all headers
//	    }
//}

@Configuration
public class WebMvcConfig {

    @Bean
    public WebMvcConfigurer getCorsConfiguration(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5500")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                		.allowCredentials(true);
            }
        };
    }
}