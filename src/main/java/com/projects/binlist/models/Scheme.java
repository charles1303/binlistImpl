package com.projects.binlist.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="schemes")
public class Scheme extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4444286568916778452L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
