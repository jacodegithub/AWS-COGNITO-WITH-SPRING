package com.brpl.kyc.demo.model;

public enum Approve {
	
	APPROVED("Approved"),
	NOT_APPROVED("Not Approved");

	private String status;
	
	Approve(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return this.status;
	}
}
