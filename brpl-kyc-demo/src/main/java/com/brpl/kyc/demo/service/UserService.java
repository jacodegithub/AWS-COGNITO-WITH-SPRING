package com.brpl.kyc.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brpl.kyc.demo.model.User;
import com.brpl.kyc.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	public void createUser(User user) {
		userRepo.save(user);
	}
}
