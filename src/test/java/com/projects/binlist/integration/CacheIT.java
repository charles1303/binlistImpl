package com.projects.binlist.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.projects.binlist.repositories.CardDetailRequestLogRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheIT {
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired 
	private CardDetailRequestLogRepository repo;
	
	@Test
	public void validateCache() {
		Cache hits = this.cacheManager.getCache("hits:per:card:number");
		assertThat(hits).isNotNull();
		hits.clear(); // Simple test assuming the cache is empty.
		assertThat(hits.get("getCardRequestLogsCountGroupedByCardNumber")).isNull();
		Object be = this.repo.getCardRequestLogsCountGroupedByCardNumber(new PageRequest(0, 1));
		assertThat(hits.get("getCardRequestLogsCountGroupedByCardNumber")).isNotNull();
}

}
