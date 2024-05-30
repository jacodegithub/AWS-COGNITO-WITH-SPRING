package com.brpl.kyc.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brpl.kyc.demo.model.KYCRequest;
import com.brpl.kyc.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
