package com.brpl.kyc.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.brpl.kyc.demo.FileHandling;
import com.brpl.kyc.demo.model.KYCRequest;
import com.brpl.kyc.demo.model.KYCUser;
import com.brpl.kyc.demo.model.User;
import com.brpl.kyc.demo.repository.KYCRequestRepository;
import com.brpl.kyc.demo.repository.KYCUserRepository;
import com.brpl.kyc.demo.repository.UserRepository;
import com.brpl.kyc.demo.service.KYCService;
import com.brpl.kyc.demo.service.UserService;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins="http://localhost:5500")
public class UserController {
	
    @Autowired
    private AmazonS3 amazonS3Client;

	@Autowired
	private KYCService kycService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileHandling handleFile;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private KYCRequestRepository kycRepo;
	
	@Autowired
	private KYCUserRepository kycUserRepo;
	
	@Value("${upload.image}")
	private String path;
	
	private static final String BUCKET_NAME = "brpl-aadhar-pan-bucket";
	
	private Logger log = LoggerFactory.getLogger(UserController.class);
	
	@GetMapping("/check")
	public String testing() {
		return "Hell, yeah, it is working!";
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> addUser(@RequestBody User user) {
		try {
			userService.createUser(user);
			log.info("this is the user : {} ",user.getFirstName() );
			return ResponseEntity.ok(user);
		} catch(Exception ex) {
//			return ResponseEntity.badRequest().body("Failed to create user: " + ex.getMessage());
			return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR).ok().body("Failed to create user: " + ex.getMessage());
		}
	}
	
	@PostMapping("/kycData")
	public ResponseEntity<?> creatingUserKycData(@RequestBody KYCUser kyc) {
		KYCUser kycUser = kycUserRepo.save(kyc);
		return ResponseEntity.ok(kycUser);
	}
	
	@GetMapping("/{email}")
	public ResponseEntity<?> getEmailOfUser(@PathVariable String email) {
		User user = null;
		try {
			user = userRepo.findByEmail(email);			
			return ResponseEntity.ok(user);
		} catch(RuntimeException ex) {
			return ResponseEntity.badRequest().body("Invalid user emailId: " + ex.getMessage());
		}
	}
	
	@PostMapping("/{userId}/kyc")
	public ResponseEntity<?> submitKycRequest(@PathVariable Long userId,
											@RequestParam("aadharImage") MultipartFile aadharImage,
											@RequestParam("pancardImage") MultipartFile pancardImage
										) {
		
		try {
//			String afname = handleFile.uploadImage(aadharImage, path);
//			String pfname = handleFile.uploadImage(pancardImage, path);
			
            // Upload Aadhar image to S3
            String afname = uploadToS3(aadharImage, "aadhar", userId);

            // Upload Pancard image to S3
            String pfname = uploadToS3(pancardImage, "pancard", userId);
			
			kycService.submitKycRequest(userId, afname, pfname);
			
			return ResponseEntity.ok().build();
		} catch(Exception ex) {
			return ResponseEntity.badRequest().body("Failed to submit KYC request: " + ex.getMessage());
		}		
	}
	
	
	@GetMapping("/{email}/kyc/getImages")
	public ResponseEntity<?> getKycUserImages(@PathVariable String email) {
		User user = userRepo.findByEmail(email);
		
		InputStream aadharImageStream = downloadFromS3("aadhar", user.getId());
		InputStream pancardImageStream = downloadFromS3("pancard", user.getId());
		
		// Convert input streams to byte arrays
	    byte[] aadharImageBytes = inputStreamToByteArray(aadharImageStream);
	    byte[] pancardImageBytes = inputStreamToByteArray(pancardImageStream);
	    
	 // You can return the byte arrays, URLs, or Base64 encoded strings of the images
	    // Here, we are returning byte arrays
	    Map<String, byte[]> images = new HashMap<>();
	    images.put("aadhar", aadharImageBytes);
	    images.put("pancard", pancardImageBytes);
	    
	    return ResponseEntity.ok(images);
//		return ResponseEntity.ok("it is working");
	}
	private byte[] inputStreamToByteArray(InputStream inputStream) {
	    try {
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[4096];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outputStream.write(buffer, 0, bytesRead);
	        }
	        return outputStream.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
	
	@GetMapping("/{userId}/getKyc")
	public ResponseEntity<?> getUserKyc(@PathVariable Long userId) {
		try {
			
			KYCRequest kycUserRequest = kycService.getKycRequestByUserId(userId);
			
			return ResponseEntity.ok(kycUserRequest);
		} catch(Exception ex) {
			return ResponseEntity.badRequest().body("Failed to fetch KYC request: " + ex.getMessage());
		}
	}
	
	
	@PostMapping("/{userId}/kyc/approve")
	public ResponseEntity<?> approveUserKyc(@PathVariable Long userId) {
		kycService.approveUserKyc(userId);
		return ResponseEntity.ok("User is Approved");
	}
	
	@GetMapping("/kyc/all")
	public ResponseEntity<?> getAllKycUsers() {
		List<KYCUser> kycUlist = kycUserRepo.findAll();
		return ResponseEntity.ok(kycUlist);
	}
	
	
    private String uploadToS3(MultipartFile file, String type, Long userId) throws IOException {
        String keyName = userId + "/" + type + ".jpg"; // Example keyName: 123/aadhar.jpg
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg"); // Set content type if necessary
        metadata.setContentLength(file.getSize()); // Set content length

        amazonS3Client.putObject(new PutObjectRequest(BUCKET_NAME, keyName, file.getInputStream(), metadata));
        return keyName; // Return the S3 key for the uploaded file
    }
	
    // DOWNLOAD OR SERVE USER IMAGES FROM AWS S3
    private InputStream downloadFromS3(String type, Long userId) {
    	String keyName = userId + "/" + type + ".jpg";
	    GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, keyName);
	    S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
	    return s3Object.getObjectContent();
	}
}
