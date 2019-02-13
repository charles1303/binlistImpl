package com.projects.binlist.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.projects.binlist.dto.responses.CardRequestLogDto;
import com.projects.binlist.services.CardDetailService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CardDetailControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockBean
	private CardDetailService service;
	
	@Test
    public void givenStartAndLimit_whenGetCardHits_thenReturnValidJsonObject() throws Exception{
    	int start = 0;
    	int limit = 3;
    	String expected = "{\n" + 
    			"    \"success\": true,\n" + 
    			"    \"start\": 0,\n" + 
    			"    \"limit\": 3,\n" + 
    			"    \"size\": 1,\n" + 
    			"    \"payload\": {\n" + 
    			"        \"45717260\": \"10\"\n" + 
    			"    }\n" + 
    			"}";
    	CardRequestLogDto dto = new CardRequestLogDto();
    	dto.setSuccess(true);
    	dto.setStart(0);
    	dto.setLimit(3);
    	dto.setSize(1);
    	Map<String, String> payload = new HashMap<>();
    	payload.put("45717260", "10");
    	dto.setPayload(payload);
    	Mockito.when(service.getCardRequestLogsCountGroupedByCard(any())).thenReturn(dto);
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
    			"/card-scheme/stats?start="+start+"&limit="+limit).accept(
				MediaType.APPLICATION_JSON);
    	
    	MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    	MockHttpServletResponse response = result.getResponse();
    	assertEquals(HttpStatus.OK.value(), response.getStatus());
    	
    	JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);
				
    	
    	
    }

}
