package com.projects.binlist.dto.responses;

import java.io.Serializable;

public class CardDetailDto extends Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2182517566296530664L;

	private boolean success = false;
	
	private CardDetailPayloadDto payload;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public CardDetailPayloadDto getPayload() {
		return payload;
	}

	public void setPayload(CardDetailPayloadDto payload) {
		this.payload = payload;
	}
}
