package com.brpl.kyc.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brpl.kyc.demo.model.Approver;

public interface ApproverRepository extends JpaRepository<Approver, Long> {

}
