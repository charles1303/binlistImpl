package com.projects.binlist.models;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.projects.binlist.models.BaseModel;

@Entity
@Table(name="countries")
public class Country extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8732847522296998485L;
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
