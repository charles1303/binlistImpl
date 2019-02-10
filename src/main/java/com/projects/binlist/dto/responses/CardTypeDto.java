package com.projects.binlist.dto.responses;

public enum CardTypeDto {
	CREDIT("credit"), DEBIT("debit");
	
	private String value;
	
	CardTypeDto(String value) { this.setValue(value); }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
