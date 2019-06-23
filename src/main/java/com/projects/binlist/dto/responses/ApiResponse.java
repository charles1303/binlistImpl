package com.projects.binlist.dto.responses;

import java.io.Serializable;

public class ApiResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5370501849770833160L;
	private int status;
	private String message;
	private Object result;

	public ApiResponse(int status, String message, Object result){
	    this.status = status;
	    this.message = message;
	    this.result = result;
    }

    public ApiResponse(int status, String message){
        this.status = status;
        this.message = message;
    }

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
