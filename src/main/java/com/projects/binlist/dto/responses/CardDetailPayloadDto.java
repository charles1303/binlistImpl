package com.projects.binlist.dto.responses;

import java.io.Serializable;

public class CardDetailPayloadDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1001016902543552102L;

	private String scheme;
	
	private String type;
	
	private String bank;

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}
}
