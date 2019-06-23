package com.projects.binlist.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import com.projects.binlist.dto.responses.CardDetailDto;
import com.projects.binlist.dto.responses.CardRequestLogDto;
import com.projects.binlist.exceptions.GeneralException;
import com.projects.binlist.exceptions.RecordNotFoundException;
import com.projects.binlist.services.CardDetailService;

@RestController
@RequestMapping("/")
public class CardDetailController {
	
	Logger logger = LoggerFactory.getLogger(CardDetailController.class);
	 
	
	@Autowired
	private CardDetailService cardDetailService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/card-scheme/stats", params = { "start", "limit" }, method = {RequestMethod.GET})
	public CardRequestLogDto getCardDetail(@RequestParam("start") int start, @RequestParam("limit") int limit) throws GeneralException, RecordNotFoundException {
		
		return cardDetailService.getCardRequestLogsCountGroupedByCard(new PageRequest(start, limit));
		
	}
	
	@RequestMapping(value = "/card-scheme/verify/{iinStart}", method = {RequestMethod.GET})
	public CardDetailDto verifyCardDetail(@PathVariable("iinStart") String iinStart) throws HttpStatusCodeException{
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return cardDetailService.verifyCardDetail(iinStart, entity);
        
	}
	
	@RequestMapping(value = "/hystrix/card-scheme/verify/{iinStart}", method = {RequestMethod.GET})
	public CardDetailDto verifyCardDetailHysterix(@PathVariable("iinStart") String iinStart) throws HttpStatusCodeException{
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return cardDetailService.verifyCardDetailHystrix(iinStart, entity);
        
	}
	
}
