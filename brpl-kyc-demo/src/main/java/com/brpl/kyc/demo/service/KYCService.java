package com.brpl.kyc.demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.brpl.kyc.demo.controller.UserController;
import com.brpl.kyc.demo.model.Approve;
import com.brpl.kyc.demo.model.KYCRequest;
import com.brpl.kyc.demo.model.KYCUser;
import com.brpl.kyc.demo.model.User;
import com.brpl.kyc.demo.repository.KYCRequestRepository;
import com.brpl.kyc.demo.repository.KYCUserRepository;
import com.brpl.kyc.demo.repository.UserRepository;

@Service
public class KYCService {

	@Autowired 
	private KYCRequestRepository kycRequestRepo;
	@Autowired 
	private UserRepository userRepo;
	
	@Autowired
	private KYCUserRepository kycUser;
	
	private Logger log = LoggerFactory.getLogger(KYCService.class);
	
	public void submitKycRequest(Long userId, String aadharImage, String pancardImage) throws IOException {
		User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Invalid userId "+userId));
		
		KYCRequest kycRequest = new KYCRequest();
		kycRequest.setUser(user);
		kycRequest.setAadharImage(aadharImage);
		kycRequest.setPancardImage(pancardImage);
		
		kycRequestRepo.save(kycRequest);
	}
	
	public KYCRequest getKycRequestByUserId(Long userId) {
		User user = userRepo.findById(userId).orElseThrow(
				() -> new RuntimeException("Invalid userid "+userId)
		);
		
		KYCRequest kycRequest = kycRequestRepo.findByUser(user);
		
		return kycRequest;
	}
	
	public void approveUserKyc(Long userId) {
		KYCUser kyc = kycUser.findIdByUserid(userId);
		System.out.println(kyc);
		kyc.setApproved(Approve.APPROVED);
		kycUser.save(kyc);
	}
}
