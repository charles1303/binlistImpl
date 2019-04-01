package com.projects.binlist.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.axonframework.eventhandling.EventHandler;
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

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.projects.binlist.components.CardDetailCommandComponent;
import com.projects.binlist.dto.responses.BinListResponse;
import com.projects.binlist.dto.responses.CardDetailDto;
import com.projects.binlist.dto.responses.CardDetailPayloadDto;
import com.projects.binlist.dto.responses.CardRequestLogDto;
import com.projects.binlist.eventsourcing.events.CardDetailRequestCreatedEvent;
import com.projects.binlist.exceptions.GeneralException;
import com.projects.binlist.exceptions.RecordNotFoundException;
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
	
	@Autowired
	private CardDetailCommandComponent cardDetailCommandComponent;

	public CardDetailRequestLogRepository getCardDetailRequestLogRepository() {
		return cardDetailRequestLogRepository;
	}

	public void setCardDetailRequestLogRepository(CardDetailRequestLogRepository cardDetailRequestLogRepository) {
		this.cardDetailRequestLogRepository = cardDetailRequestLogRepository;
	}

	public CardDetailDto verifyCardDetail(String iinStart) {

		return convertToCardDetailDto(cardDetailRepository.findByIinStart(iinStart));
	}

	@EventHandler
	public void logCardDetailRequest(CardDetailRequestCreatedEvent cardDetailRequestCreatedEvent) {
		CardDetailRequestLog requestLog = new CardDetailRequestLog();
		requestLog.setCardNumber(cardDetailRequestCreatedEvent.getCardNumber());
		requestLog.setRequestDate(cardDetailRequestCreatedEvent.getRequestDate());
		cardDetailRequestLogRepository.save(requestLog);
	}
	
	@Async
	@CacheEvict(value = "cache", allEntries = true)
	public void logCardDetailRequest(String iinStart) {
		CardDetailRequestLog requestLog = new CardDetailRequestLog();
		requestLog.setCardNumber(iinStart);
		cardDetailRequestLogRepository.save(requestLog);
	}

	@Cacheable(value = "cache", key = "#root.method.name+ '_' +#pageable.pageNumber")
	public CardRequestLogDto getCardRequestLogsCountGroupedByCard(Pageable pageable)
			throws GeneralException, RecordNotFoundException {
		CardRequestLogDto cardRequestLogDto = new CardRequestLogDto();
		Page<Map<String, Object>> page = cardDetailRequestLogRepository
				.getCardRequestLogsCountGroupedByCardNumber(pageable);
		if (null == page) {
			throw new GeneralException();
		}
		if (1 > page.getSize()) {
			throw new RecordNotFoundException();
		}
		if (null != page) {
			cardRequestLogDto.setStart(page.getNumber());
			cardRequestLogDto.setLimit(page.getSize());
			cardRequestLogDto.setSize(page.getTotalElements());
			if (page.hasContent()) {
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

	private CardDetailDto convertToCardDetailDto(CardDetail cardDetail) {
		CardDetailDto cardDetailDto = new CardDetailDto();
		if (null != cardDetail) {
			cardDetailDto.setPayload(new CardDetailPayloadDto());
			cardDetailDto.getPayload().setBank(cardDetail.getBank() != null ? cardDetail.getBank().getName() : "");
			cardDetailDto.getPayload()
					.setScheme(cardDetail.getScheme() != null ? cardDetail.getScheme().getName() : "");
			cardDetailDto.getPayload().setType(cardDetail.getType() != null ? cardDetail.getType().getValue() : "");
			cardDetailDto.setSuccess(true);
		}
		return cardDetailDto;
	}

	public CardDetailDto convertToCardDetailDto(BinListResponse binListResponse) {
		CardDetailDto cardDetailDto = new CardDetailDto();
		if (!(null == binListResponse)) {
			cardDetailDto.setPayload(new CardDetailPayloadDto());
			cardDetailDto.getPayload()
					.setBank(binListResponse.getBank() != null ? binListResponse.getBank().getName() : "");
			cardDetailDto.getPayload()
					.setScheme(binListResponse.getScheme() != null ? binListResponse.getScheme() : "");
			cardDetailDto.getPayload().setType(binListResponse.getType() != null ? binListResponse.getType() : "");
			cardDetailDto.setSuccess(true);
		}
		return cardDetailDto;
	}

	public CardDetailDto verifyCardDetail(String iinStart, HttpEntity<?> entity) throws HttpStatusCodeException {
		logCardDetailRequest(iinStart);

		ResponseEntity<BinListResponse> response = null;
		BinListResponse binListResponse = null;
		try {
			response = restTemplate.exchange(binlistURL + "/{iinStart}", HttpMethod.GET, entity, BinListResponse.class,
					iinStart);
			binListResponse = response.getBody();
		} catch (HttpStatusCodeException e) {
			throw e;
		}

		return convertToCardDetailDto(binListResponse);
	}

	@HystrixCommand(fallbackMethod = "defaultCardDetailDto", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
	public CardDetailDto verifyCardDetailHystrix(String iinStart, HttpEntity<?> entity) throws HttpStatusCodeException {
		cardDetailCommandComponent.createCardDetalRequestLog(iinStart, entity);
		
		ResponseEntity<BinListResponse> response = null;
		BinListResponse binListResponse = null;
		try {
			response = restTemplate.exchange(binlistURL + "/{iinStart}", HttpMethod.GET, entity, BinListResponse.class,
					iinStart);
			binListResponse = response.getBody();
		} catch (HttpStatusCodeException e) {
			throw e;
		}

		return convertToCardDetailDto(binListResponse);
	}
	
	public CardDetailDto defaultCardDetailDto(String iinStart, HttpEntity<?> entity) {

		// Or you can return data from the cache store we configured using
		// the CacheManager bean

		CardDetailDto cardDetailDto = new CardDetailDto();
		cardDetailDto.setPayload(new CardDetailPayloadDto());
		cardDetailDto.getPayload().setBank("ISA");
		cardDetailDto.getPayload().setScheme("VISA");
		cardDetailDto.getPayload().setType("DEBIT");
		cardDetailDto.setSuccess(true);
		return cardDetailDto;
	}
	
}
