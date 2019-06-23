package com.projects.binlist.utils;

public enum RoleType {
	ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER");
	
	private String value;
	
	RoleType(String value) { this.setValue(value); }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
