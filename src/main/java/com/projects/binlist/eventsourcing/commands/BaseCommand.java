package com.projects.binlist.eventsourcing.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class BaseCommand<T> {

    @TargetAggregateIdentifier
    private final T id;

	public BaseCommand(T id) {
        this.id = id;
    }

    public T getId() {
		return id;
	}


}
