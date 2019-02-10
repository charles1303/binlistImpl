package com.projects.binlist.dto.responses;

import java.io.Serializable;

public class Number implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1848092139956150750L;

	private int length;
	
    private boolean luhn;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isLuhn() {
		return luhn;
	}

	public void setLuhn(boolean luhn) {
		this.luhn = luhn;
	}
}
