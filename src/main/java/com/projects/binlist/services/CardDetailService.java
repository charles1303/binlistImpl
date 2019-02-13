package com.projects.binlist.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.projects.binlist.dto.responses.BinListResponse;
import com.projects.binlist.dto.responses.CardDetailDto;
import com.projects.binlist.dto.responses.CardDetailPayloadDto;
import com.projects.binlist.dto.responses.CardRequestLogDto;
import com.projects.binlist.models.CardDetail;
import com.projects.binlist.models.CardDetailRequestLog;
import com.projects.binlist.repositories.CardDetailRepository;
import com.projects.binlist.repositories.CardDetailRequestLogRepository;

@Service
public class CardDetailService {
	
	Logger logger = LoggerFactory.getLogger(CardDetailService.class);
	
	@Value("${binlist.url}")
	 String binlistURL;
	
	@Autowired
	private CardDetailRepository cardDetailRepository;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	private CardDetailRequestLogRepository cardDetailRequestLogRepository;
	
	public CardDetailDto verifyCardDetail(String iinStart) {
		
		return convertToDto(cardDetailRepository.findByIinStart(iinStart));
    }
	
	@Async
	@CacheEvict(value = "hits:per:card:number", key="'getCardRequestLogsCountGroupedByCardNumber'")
	public void logCardDetailRequest(String iinStart) {
		CardDetailRequestLog requestLog = new CardDetailRequestLog();
		requestLog.setCardNumber(iinStart);
		cardDetailRequestLogRepository.save(requestLog);
	}
	
	@Cacheable(value = "hits:per:card:number", key = "#root.methodName")
	public CardRequestLogDto getCardRequestLogsCountGroupedByCard(Pageable pageable) {
		CardRequestLogDto cardRequestLogDto = null;
		Page<Map<String, Object>> page =  cardDetailRequestLogRepository.getCardRequestLogsCountGroupedByCardNumber(pageable);
		if(page != null) {
			cardRequestLogDto = new CardRequestLogDto();
			cardRequestLogDto.setStart(page.getNumber());
			cardRequestLogDto.setLimit(page.getSize());
			cardRequestLogDto.setSize(page.getTotalElements());
			if(page.hasContent()) {
				cardRequestLogDto.setSuccess(true);
				Map<String, String> data = new ConcurrentHashMap<>();
				for (Map<String, Object> obj : page.getContent()) {
					data.put(String.valueOf(obj.get("cardNumber")), String.valueOf(obj.get("count")));
				}
				cardRequestLogDto.setPayload(data);
			}
		}
		
		return cardRequestLogDto;
	}
	
	private CardDetailDto convertToDto(CardDetail cardDetail) {
		CardDetailDto cardDetailDto = new CardDetailDto();
		if(cardDetail != null) {
			cardDetailDto.setPayload(new CardDetailPayloadDto());
			cardDetailDto.getPayload().setBank(cardDetail.getBank() != null ? cardDetail.getBank().getName() : "");
			cardDetailDto.getPayload().setScheme(cardDetail.getScheme() != null ?cardDetail.getScheme().getName() : "");
			cardDetailDto.getPayload().setType(cardDetail.getType() != null ? cardDetail.getType().getValue() : "");
			cardDetailDto.setSuccess(true);
		}
		return cardDetailDto;
	}
	
	public CardDetailDto convertToDto(BinListResponse binListResponse) {
		CardDetailDto cardDetailDto = new CardDetailDto();
		if(!(binListResponse == null)) {
			cardDetailDto.setPayload(new CardDetailPayloadDto());
			cardDetailDto.getPayload().setBank(binListResponse.getBank() != null ? binListResponse.getBank().getName() : "");
			cardDetailDto.getPayload().setScheme(binListResponse.getScheme() != null ? binListResponse.getScheme() : "");
			cardDetailDto.getPayload().setType(binListResponse.getType() != null ? binListResponse.getType() : "");
			cardDetailDto.setSuccess(true);
		}
		return cardDetailDto;
	}
	
	public CardDetailDto verifyCardDetail(String iinStart, HttpEntity<?> entity) {
		logCardDetailRequest(iinStart);
		
        ResponseEntity<BinListResponse> response = null;
        BinListResponse binListResponse = null;
        try {
        	response = restTemplate.exchange(binlistURL +"/{iinStart}",HttpMethod.GET, entity,BinListResponse.class, iinStart);
        	binListResponse = response.getBody();
        }catch (HttpStatusCodeException e) {
        	logger.error("Error processing request ", e);
		}
        
		
		return convertToDto(binListResponse);
	}
	
}
