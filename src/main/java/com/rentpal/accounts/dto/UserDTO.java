package com.rentpal.accounts.dto;

public class UserDTO {
	private Long userid;
	
	private String email;
	
	private String creationTime;
	
	private boolean verified;
	
	public Long getUserid() {
		return userid;
	}
	
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreationTime() { return creationTime; }

	public void setCreationTime(String creationTime) { this.creationTime = creationTime; }

	public boolean isVerified() {
		return verified;
	}
	
	public void setVerified(boolean verified) {
		this.verified = verified;
	}	
}
