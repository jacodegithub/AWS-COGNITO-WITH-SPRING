package com.aws.cognito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.aws.cognito.service.CognitoService;


@SpringBootApplication
@RestController
public class DemoApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Autowired
	private CognitoService cogService;
	
	@GetMapping("/data")
	public String checkingForAccess() {
		return "It is working fine..!!";
	}
	
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome To The Index Page!");
        return "index.html";
    }

    @GetMapping("admin/greetMe")
    public String adminGreet(Model model) {
        String response = "Welcome admin ! You developed an amazing website! :)";

        model.addAttribute("response", response);

        return "greetings to admin";
    }

    @GetMapping("user/greetMe")
    public String userGreet(Model model) {

        String response = "Welcome user ! God bless you with amazing future ahead! :)";

        model.addAttribute("response", response);

        return "greetings to user";
    }
    
    
    /*
     * adding users to aws cognito user pool only by admin
     */
    
//    @CrossOrigin(origins = "http://localhost:5500")
    
//    @Autowired
//    private RestTemplate restTemplate;

    // Define your API Gateway endpoint URL
    private static final String API_GATEWAY_URL = "https://your-api-gateway-endpoint.amazonaws.com/cognito-proxy";
    
    
//    @PostMapping("/admin/register-user")
//    public String registerUser(@RequestBody User user) {
//    	try {
//    		cogService.addUserToUserPool(user.getUsername(), user.getEmail(), user.getPassword());
//    		return "Registratoin-Successful";
//    	}catch(Exception ex) {	
//    		return "Registration-Error!";
//    	}
//    }
    
//    @PostMapping("/user-details")
//    public String enterUserDetails(@RequestBody User user) {
//    	cogService.addUserToUserPool(user.getUsername(), user.getEmail(), user.getPassword());
//    	return "user-added-successfully!!";
//    }

}
