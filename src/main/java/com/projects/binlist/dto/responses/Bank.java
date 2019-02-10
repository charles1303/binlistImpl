package com.projects.binlist.dto.responses;

import java.io.Serializable;

public class Bank implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5266405135272340762L;

	private String name;
	
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
