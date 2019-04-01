package com.projects.binlist.integration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import com.projects.binlist.components.CardDetailCommandComponent;
import com.projects.binlist.dto.responses.CardRequestLogDto;
import com.projects.binlist.exceptions.GeneralException;
import com.projects.binlist.exceptions.RecordNotFoundException;
import com.projects.binlist.repositories.CardDetailRepository;
import com.projects.binlist.repositories.CardDetailRequestLogRepository;
import com.projects.binlist.services.CardDetailService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners(listeners = {SpringBootDependencyInjectionTestExecutionListener.class, ServletTestExecutionListener.class})
public class CacheIntegrationTest {
	
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	
	@Autowired
	CardDetailRequestLogRepository testCardDetailRequestLogRepository;
	
	@Autowired
	CardDetailService testService;
	
	@Autowired
	private CardDetailRepository testCardDetailRepository;
	
	@Autowired
	private CardDetailCommandComponent testCardDetailCommandComponent;
	
	@Autowired
	RestTemplate restTemplate;
	
	
	@Configuration
    @EnableCaching
    static class SpringConfig{
        @Bean
        public CardDetailService testService(){
        	CardDetailService mockService = new CardDetailService();
        	mockService.setCardDetailRequestLogRepository(testCardDetailRequestLogRepository());
            return mockService;
        }
        @Bean
        public CardDetailRequestLogRepository testCardDetailRequestLogRepository(){
            return Mockito.mock(CardDetailRequestLogRepository.class);
        }
        
        @Bean
        public CardDetailCommandComponent testCardDetailCommandComponent(){
            return Mockito.mock(CardDetailCommandComponent.class);
        }
        
        @Bean
        public CardDetailRepository testCardDetailRepository(){
            return Mockito.mock(CardDetailRepository.class);
        }
        
        @Bean
        public RestTemplate restTemplate(){
            return new RestTemplate();
        }
        
        @Bean
        CacheManager testCacheManager() {
            return new ConcurrentMapCacheManager("cache");
        }
    }
	
	
	private static final PageRequest request = new PageRequest(0, 1);
	
	  @Test(expected = GeneralException.class)
	  public void firstCallResultShouldBeCached_AndReturned_InSecondCallResult() throws GeneralException, RecordNotFoundException {
		  // First invocation returns object returned by the method
		  CardRequestLogDto firstCallResult = testService.getCardRequestLogsCountGroupedByCard(request);
		  
		 // Second invocation should return cached value, *not* second (as set up above)
	    CardRequestLogDto secondCallResult = testService.getCardRequestLogsCountGroupedByCard(request);
	    
	    assertThat(secondCallResult, equalTo(firstCallResult));
	    
	 // Verify Repository method was called just after the two service calls
	    verify(testCardDetailRequestLogRepository, times(1)).getCardRequestLogsCountGroupedByCardNumber(request);
	    
	    exceptionRule.expect(GeneralException.class);
		exceptionRule.expectMessage("General exception. Please contact Admin!");
	    
	  }
	  
}
