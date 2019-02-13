package com.projects.binlist.unit.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.projects.binlist.dto.responses.Bank;
import com.projects.binlist.dto.responses.BinListResponse;
import com.projects.binlist.dto.responses.CardDetailDto;
import com.projects.binlist.dto.responses.CardDetailPayloadDto;
import com.projects.binlist.repositories.CardDetailRequestLogRepository;
import com.projects.binlist.services.CardDetailService;

@RunWith(MockitoJUnitRunner.class)
public class CardDetailServiceTest {
	
	@Value("https://lookup.binlist.net")
	 String binlistURL;
	
	@Mock
    private RestTemplate restTemplate;
	
	@Mock
    private CardDetailRequestLogRepository cardDetailRequestLogRepository;
	
	@InjectMocks
    private CardDetailService service;
	
	@Test
    public void givenMockingIsDoneByMockito_whenGetIsCalled_shouldReturnMockedObject() {
		String iinStart = "45717260";
		BinListResponse binListResponse = new BinListResponse();
		Bank bank = new Bank();
		bank.setName("USB");
		binListResponse.setBank(bank);
		binListResponse.setScheme("visa");
		binListResponse.setType("credit");
		
		CardDetailDto expectedDto = new CardDetailDto();
		expectedDto.setPayload(new CardDetailPayloadDto());
		expectedDto.getPayload().setBank(binListResponse.getBank().getName());
		expectedDto.getPayload().setScheme(binListResponse.getScheme());
		expectedDto.getPayload().setType(binListResponse.getType());
		expectedDto.setSuccess(true);
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
		Mockito
        .when(restTemplate.exchange(binlistURL + "/{iinStart}",HttpMethod.GET, entity,BinListResponse.class, iinStart))
        .thenReturn(new ResponseEntity(binListResponse, HttpStatus.OK));
		CardDetailDto dtoResponse = service.verifyCardDetail(iinStart, entity);
		Assert.assertEquals(expectedDto.isSuccess(), dtoResponse.isSuccess());
		Assert.assertEquals(expectedDto.getPayload().getBank(), dtoResponse.getPayload().getBank());
		Assert.assertEquals(expectedDto.getPayload().getScheme(), dtoResponse.getPayload().getScheme());
		Assert.assertEquals(expectedDto.getPayload().getType(), dtoResponse.getPayload().getType());
		
		
	}

}
