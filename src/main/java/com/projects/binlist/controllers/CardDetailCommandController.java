package com.projects.binlist.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.projects.binlist.components.CardDetailCommandComponent;

@RestController
@RequestMapping(value = "/event-sourcing")
public class CardDetailCommandController {
	
	private final CardDetailCommandComponent cardDetailCommandService;
	
	public CardDetailCommandController(CardDetailCommandComponent cardDetailCommandService) {
		this.cardDetailCommandService = cardDetailCommandService;
	}
	
	@RequestMapping(value = "/card-scheme/verify/{iinStart}", method = {RequestMethod.GET})
	public CompletableFuture<String> createCardDetalRequestLog(@PathVariable(value = "iinStart") String iinStart) {
		return cardDetailCommandService.createCardDetalRequestLog(iinStart);
	}
	
	@RequestMapping(value = "/card-scheme/verify/{iinStart}/events", method = {RequestMethod.GET})
    public List<Object> listEventsForAccount(@PathVariable(value = "iinStart") String iinStart){
        return cardDetailCommandService.listEventsForAccount(iinStart);
    }

}
