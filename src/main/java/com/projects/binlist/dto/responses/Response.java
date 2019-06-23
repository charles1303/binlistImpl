package com.projects.binlist.dto.responses;

import java.io.Serializable;

public class Response implements Serializable {
	
	private int code;
	
	private String description;
	
	public Response() {
	}
	
	public Response(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
