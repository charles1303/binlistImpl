package com.projects.binlist.exceptions;

public class RequestNotFoundException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestNotFoundException(){
		super("Request Not Found!");
	}

}
