package com.projects.binlist.config;



import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.axonframework.common.jdbc.PersistenceExceptionResolver;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jdbc.JdbcSQLErrorCodesResolver;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.SQLErrorCodesResolver;
import org.axonframework.serialization.Serializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.springboot.util.RegisterDefaultEntities;
import org.axonframework.springboot.util.jpa.ContainerManagedEntityManagerProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnBean(EntityManagerFactory.class)
@RegisterDefaultEntities(packages = {"org.axonframework.eventsourcing.eventstore.jpa",
        "org.axonframework.eventhandling.tokenstore",
        "org.axonframework.eventhandling.saga.repository.jpa"})

@Configuration
public class AxonConfig {
	
	@Bean
	public EmbeddedEventStore eventStore(EventStorageEngine storageEngine, AxonConfiguration configuration) {
	    return EmbeddedEventStore.builder()
	            .storageEngine(storageEngine)
	            .messageMonitor(configuration.messageMonitor(EventStore.class, "eventStore"))
	            .build();
	}
	
	@Bean
	public EventStorageEngine eventStorageEngine(Serializer defaultSerializer,
	                       PersistenceExceptionResolver persistenceExceptionResolver,
	                       @Qualifier("eventSerializer") Serializer eventSerializer,
	                       AxonConfiguration configuration,
	                       EntityManagerProvider entityManagerProvider,
	                       TransactionManager transactionManager) {
	  return JpaEventStorageEngine.builder()
	                .snapshotSerializer(defaultSerializer)
	                .upcasterChain(configuration.upcasterChain())
	                .persistenceExceptionResolver(persistenceExceptionResolver)
	                .eventSerializer(eventSerializer)
	                .entityManagerProvider(entityManagerProvider)
	                .transactionManager(transactionManager)
	                .build();
	}

    @Bean
    public  PersistenceExceptionResolver dataSourcePersistenceExceptionResolver(DataSource dataSource) throws SQLException {
        return new SQLErrorCodesResolver(dataSource);
    }

    @Bean
    public  PersistenceExceptionResolver jdbcSQLErrorCodesResolver() {
        return new JdbcSQLErrorCodesResolver();
    }

    @Bean
    public  EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    
}
