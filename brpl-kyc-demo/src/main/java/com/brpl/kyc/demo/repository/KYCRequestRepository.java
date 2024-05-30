package com.brpl.kyc.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.brpl.kyc.demo.model.KYCRequest;
import com.brpl.kyc.demo.model.User;

public interface KYCRequestRepository extends JpaRepository<KYCRequest, Long> {
	KYCRequest findByUser(User user);
}
