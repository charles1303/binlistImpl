package com.projects.binlist.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import com.projects.binlist.utils.RoleType;

@Entity
@Table(name = "roles")
public class Role extends BaseModel {
	
	@Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private RoleType type;

	public RoleType getType() {
		return type;
	}

	public void setType(RoleType type) {
		this.type = type;
	}


}
