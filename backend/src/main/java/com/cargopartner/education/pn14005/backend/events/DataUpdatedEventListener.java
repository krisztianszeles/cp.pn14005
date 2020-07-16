package com.cargopartner.education.pn14005.backend.events;

import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cargopartner.education.pn14005.backend.elasticsearch.ElasticSearchClient;

public class DataUpdatedEventListener {
	
	@Inject
    private ElasticSearchClient elasticSearchClient;
	
	private final static String ELASTIC_INDEX = "ships";
    private final static Logger LOGGER = Logger.getLogger(DataUpdatedEventListener.class);
    
    public void onDataUpdate(@Observes(during = TransactionPhase.AFTER_SUCCESS) DataUpdatedEvent dataUpdatedEvent) {
    	switch (dataUpdatedEvent.getActionType()) {
        case CREATE:
            elasticSearchClient.createDocument(ELASTIC_INDEX, dataUpdatedEvent.getObjectId(), dataUpdatedEvent.getJsonString());
            break;
        case UPDATE:
            elasticSearchClient.updateDocument(ELASTIC_INDEX, dataUpdatedEvent.getObjectId(), dataUpdatedEvent.getJsonString());
            break;
        case DELETE:
            elasticSearchClient.deleteDocument(ELASTIC_INDEX, dataUpdatedEvent.getObjectId());
            break;
        default:
            LOGGER.info("Unsupported source for event " + dataUpdatedEvent.getActionType());
            break;
    }
    }
}
