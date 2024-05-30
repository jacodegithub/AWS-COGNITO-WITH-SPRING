package com.aws.cognito;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


//@CrossOrigin(origins = "http://localhost:5500")
@RestController
public class Controller {

	
	@PostMapping("/admin/register-user")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
	    try {
	        // Prepare the headers for the request
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Create the request entity
	        HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);

	        // Create a new RestTemplate
	        RestTemplate restTemplate = new RestTemplate();

	        // Send the request to the API Gateway proxy endpoint
	        ResponseEntity<String> responseEntity = restTemplate.exchange(
	                "https://buavgdzq2g.execute-api.us-east-1.amazonaws.com/development/{proxy+}",
	                HttpMethod.POST,
	                requestEntity,
	                String.class
	        );

	        return responseEntity;
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        return new ResponseEntity<>("Registration Error!", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
