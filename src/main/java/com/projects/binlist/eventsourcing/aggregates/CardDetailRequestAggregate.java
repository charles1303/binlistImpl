package com.projects.binlist.eventsourcing.aggregates;

import java.util.Date;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.projects.binlist.eventsourcing.commands.CreateCardDetailRequestCommand;
import com.projects.binlist.eventsourcing.events.CardDetailRequestCreatedEvent;

@Aggregate
public class CardDetailRequestAggregate {

    @AggregateIdentifier
    private String id;
    
    private String cardNumber;
	
	private Date requestDate = new Date();

	
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Date getRequestDate() {
		return requestDate;
	}
	
	public CardDetailRequestAggregate() {}
	
	@CommandHandler
	public CardDetailRequestAggregate(CreateCardDetailRequestCommand createCardDetailRequestCommand) {
		CardDetailRequestCreatedEvent cardDetailRequestCreatedEvent = new CardDetailRequestCreatedEvent(createCardDetailRequestCommand.getId(), createCardDetailRequestCommand.getCardNumber(), createCardDetailRequestCommand.getRequestDate());
		cardDetailRequestCreatedEvent.setEntity(createCardDetailRequestCommand.getEntity());
		AggregateLifecycle.apply(cardDetailRequestCreatedEvent);
	}
	
	@EventSourcingHandler
	protected void on(CardDetailRequestCreatedEvent cardDetailRequestCreatedEvent) {
		this.id = cardDetailRequestCreatedEvent.getId();
		this.cardNumber = cardDetailRequestCreatedEvent.getCardNumber();
		this.requestDate = cardDetailRequestCreatedEvent.getRequestDate();
		
	}
}
