package com.projects.binlist.models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="brands")
public class Brand extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5361987132872854500L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
