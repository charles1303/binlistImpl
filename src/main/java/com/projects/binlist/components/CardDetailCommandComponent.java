package com.projects.binlist.components;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.projects.binlist.eventsourcing.commands.CreateCardDetailRequestCommand;

@Component
public class CardDetailCommandComponent {
	
	private final CommandGateway commandGateway;
	
	private final EventStore eventStore;
	
	public CardDetailCommandComponent(CommandGateway commandGateway, EventStore eventStore) {
		this.commandGateway = commandGateway;
		this.eventStore = eventStore;
	}
	
	public CompletableFuture<String> createCardDetalRequestLog(String iinStart) {
		return commandGateway.send(new CreateCardDetailRequestCommand(UUID.randomUUID().toString(), iinStart, new Date()));
	}
	
	public List<Object> listEventsForAccount(String aggregateId) {
        return eventStore.readEvents(aggregateId).asStream().map( s -> s.getPayload()).collect(Collectors.toList());
    }
	
	public CompletableFuture<String> createCardDetalRequestLog(String iinStart, HttpEntity<?> entity) {
		CreateCardDetailRequestCommand createCardDetailRequestCommand = new CreateCardDetailRequestCommand(UUID.randomUUID().toString(), iinStart, new Date());
		createCardDetailRequestCommand.setEntity(entity);
		return commandGateway.send(createCardDetailRequestCommand);
	}

}
