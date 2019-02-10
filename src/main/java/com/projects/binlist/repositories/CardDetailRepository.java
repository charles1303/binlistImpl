package com.projects.binlist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projects.binlist.models.CardDetail;

public interface CardDetailRepository extends JpaRepository<CardDetail, Long> {
	
	public CardDetail findByIinStart(String iinStart);

}
