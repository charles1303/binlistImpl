package com.projects.binlist.repositories;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.projects.binlist.models.CardDetailRequestLog;

public interface CardDetailRequestLogRepository extends PagingAndSortingRepository<CardDetailRequestLog, Long> {
	
	@Query("SELECT c.cardNumber AS cardNumber, COUNT(c) AS count FROM CardDetailRequestLog c GROUP BY c.cardNumber")
	Page<Map<String, Object>> getCardRequestLogsCountGroupedByCardNumber(Pageable pageable);

}
