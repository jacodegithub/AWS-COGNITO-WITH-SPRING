package mom.dog.lambdas.functionss;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;

@SpringBootApplication
public class LambdaRequestHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
	@Autowired
	private CognitoIdentityProviderClient client;
	
	@Autowired
	private AdminAddUserToGroupRequest adminAddUserToGroupRequest;
	
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {

//    	adminAddUserToGroupRequest.toBuilder().groupName("USER")    	
//                .username(event.get("userName").toString())
//                .userPoolId(event.get("userPoolId").toString());
//
//        client.adminAddUserToGroup(adminAddUserToGroupRequest);
//
//        context.getLogger().log("User " + event.get("userName").toString() + " Added to group USER");
//        
//        return event;
    	
    	try (CognitoIdentityProviderClient client = CognitoIdentityProviderClient.create()) {
            AdminAddUserToGroupRequest adminAddUserToGroupRequest = AdminAddUserToGroupRequest.builder()
                    .groupName("USER")
                    .username(event.get("userName").toString())
                    .userPoolId(event.get("userPoolId").toString())
                    .build();

            client.adminAddUserToGroup(adminAddUserToGroupRequest);

            context.getLogger().log("User " + event.get("userName").toString() + " Added to group USER");
        } catch (Exception exception) {
            context.getLogger().log("Failed to add user " + event.get("userName").toString() + "Reason: " + exception.getMessage());
        }
        return event;
    }
    

	public static void main(String[] args) {
		SpringApplication.run(LambdaRequestHandler.class, args);
	}
	
	@Bean
	public CognitoIdentityProviderClient cognitoIdentityProviderClient() {
		return CognitoIdentityProviderClient.builder().region(Region.US_EAST_1).build();
	}
	
	@Bean
	public AdminAddUserToGroupRequest adminAddUserToGroupRequest() {
		return AdminAddUserToGroupRequest.builder().build();
	}
}
