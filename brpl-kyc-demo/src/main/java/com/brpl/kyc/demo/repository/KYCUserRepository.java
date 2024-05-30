package com.brpl.kyc.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.brpl.kyc.demo.model.KYCRequest;
import com.brpl.kyc.demo.model.KYCUser;

public interface KYCUserRepository extends JpaRepository<KYCUser, Long> {

	@Query("select kyc from KYCUser kyc where kyc.id=:userId ")
	KYCUser findIdByUserid(Long userId);

	List<KYCUser> findAll();
}
