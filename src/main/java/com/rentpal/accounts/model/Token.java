package com.rentpal.accounts.model;

import javax.persistence.*;

/**
 * Verification table stores tokens for verify and password reset.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 12, 2020 8:15:17 PM
 */
@Entity
public class Token {

	@EmbeddedId
	TokenId tokenId;

	@Column(nullable = false, unique = true)
	private String id;

	@Column(nullable = false)
	private Long creationTime;

	public TokenId getTokenId() { return tokenId; }

	public void setTokenId(TokenId tokenId) { this.tokenId = tokenId; }

	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public Long getCreationTime() { return creationTime; }

	public void setCreationTime(Long creationTime) { this.creationTime = creationTime; }

}
