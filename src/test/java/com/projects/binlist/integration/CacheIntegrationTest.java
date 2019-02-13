package com.projects.binlist.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.projects.binlist.dto.responses.CardRequestLogDto;
import com.projects.binlist.repositories.CardDetailRequestLogRepository;
import com.projects.binlist.services.CardDetailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CacheIntegrationTest {
	
	@Mock
    private CardDetailRequestLogRepository cardDetailRequestLogRepository;
	
	@InjectMocks
	private CardDetailService service;
	
	private static final PageRequest request = new PageRequest(0, 1);
	
	  @Test
	  public void methodInvocationShouldBeCached() {
		  // First invocation returns object returned by the method
		  CardRequestLogDto result = service.getCardRequestLogsCountGroupedByCard(request);
	    
		 // Second invocation should return cached value, *not* second (as set up above)
	    CardRequestLogDto result2 = service.getCardRequestLogsCountGroupedByCard(request);
	    
	    
	    assertThat(result2, equalTo(result));
	  }
	  
}
