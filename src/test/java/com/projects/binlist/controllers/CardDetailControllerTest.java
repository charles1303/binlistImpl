package com.projects.binlist.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.projects.binlist.services.CardDetailService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CardDetailControllerTest {
	
	
	@Autowired
    private MockMvc mockMvc;
 
	@Test
    public void givenCardNumber_whenVerifyCardDetails_thenReturnValidJsonObject() throws Exception{
    	String iinStart = "45717260";
    	
    	CardDetailService serviceSpy = spy(new CardDetailService());
    	doNothing().when(serviceSpy).logCardDetailRequest(iinStart);
    	this.mockMvc.perform(get("/card-scheme/verify/" + iinStart))
                .andExpect(status().isOk())
    			.andExpect(jsonPath("$.success").exists())
    			.andExpect(jsonPath("$.payload").exists())
    			.andExpect(jsonPath("$.payload.scheme").exists())
    			.andExpect(jsonPath("$.payload.type").exists())
    			.andExpect(jsonPath("$.payload.bank").exists());
    }
	
	@Test
    public void givenInvalidCardNumber_whenVerifyCardDetails_thenReturnExpectedJsonObject() throws Exception{
    	String iinStart = "xxxxxx45717260";
    	
    	CardDetailService serviceSpy = spy(new CardDetailService());
    	doNothing().when(serviceSpy).logCardDetailRequest(iinStart);
    	this.mockMvc.perform(get("/card-scheme/verify/" + iinStart))
                .andExpect(status().isOk())
    			.andExpect(jsonPath("$.success").exists())
    			.andExpect(jsonPath("$.payload").doesNotExist());
    }
	
	@Test
    public void givenStartAndLimit_whenGetCardHits_thenReturnValidJsonObject() throws Exception{
    	int start = 0;
    	int limit = 3;
    	
    	this.mockMvc.perform(get("/card-scheme/stats?start="+start+"&limit="+limit))
                .andExpect(status().isOk())
    			.andExpect(jsonPath("$.success").exists())
    			.andExpect(jsonPath("$.start").exists())
    			.andExpect(jsonPath("$.limit").exists())
    			.andExpect(jsonPath("$.size").exists())
    			.andExpect(jsonPath("$.payload").exists())
    			.andExpect(MockMvcResultMatchers.jsonPath("$.start").value(start))
    			.andExpect(MockMvcResultMatchers.jsonPath("$.limit").value(limit));
    }

}
