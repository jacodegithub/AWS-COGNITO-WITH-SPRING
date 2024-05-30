package com.brpl.kyc.demo.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="kyc_request")
@NoArgsConstructor
@AllArgsConstructor
public class KYCRequest {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    private User user;
    
    @ManyToOne
    private Approver approver;
    
    private boolean approved;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String aadharImage;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String pancardImage;

    
    public KYCRequest() {}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Approver getApprover() {
		return approver;
	}

	public void setApprover(Approver approver) {
		this.approver = approver;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String AadharImage() {
		return aadharImage;
	}

	public void setAadharImage(String aadharImageData) {
		this.aadharImage = aadharImageData;
	}

	public String getAadharImage() {
		return aadharImage;
	}
	
	public String getPancardImage() {
		return pancardImage;
	}

	public void setPancardImage(String pancardImage) {
		this.pancardImage = pancardImage;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
