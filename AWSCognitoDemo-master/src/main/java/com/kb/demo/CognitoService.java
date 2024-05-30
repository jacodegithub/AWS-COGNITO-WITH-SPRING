package com.kb.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

@Service
public class CognitoService {

    @Value("${cognito.userPoolId}")
    private String userPoolId;
    
    @Value("${aws.accessKey}")
    private String accessKey;
    
    @Value("${aws.secretkey}")
    private String secretKey;

//    
//    @Autowired
//    private CognitoIdentityProviderClient cognitoClient;
    

    public void addUserToUserPool(String username, String email, String password) {
    	
    	// Create a credentials provider
    	StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));

    	
    	CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                //.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
    			.credentialsProvider(credentialsProvider)
                .region(Region.US_EAST_1)
                .build();
    	
    	
    	// Create user in the user pool
    	AdminCreateUserRequest createUserRequest = AdminCreateUserRequest.builder()
    	        .userPoolId(userPoolId)
    	        .username(username)
    	        .temporaryPassword(password) // Temporary password for the user
    	        .userAttributes(AttributeType.builder().name("email").value(email).build())
    	        .build();
    	
    	
    	// Add user to the "USER" group
        AdminAddUserToGroupRequest addUserToGroupRequest = AdminAddUserToGroupRequest.builder()
                .userPoolId(userPoolId)
                .username(username)
                .groupName("USER")
                .build();
        
        

        AdminCreateUserResponse createUserResponse = cognitoClient.adminCreateUser(createUserRequest);
        System.out.println("User creation response: " + createUserResponse);

        

        AdminAddUserToGroupResponse addUserToGroupResponse = cognitoClient.adminAddUserToGroup(addUserToGroupRequest);
        
//        AdminConfirmSignUpRequest confirmSignUpRequest = AdminConfirmSignUpRequest.builder()
//        		.userPoolId(userPoolId)
//        		.username(username)
//        		.build();
//        
//        cognitoClient.adminConfirmSignUp(confirmSignUpRequest);
        
        System.out.println("User added to group response: " + addUserToGroupResponse);
    }
}
