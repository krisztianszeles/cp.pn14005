package com.cargopartner.education.pn14005.backend.elasticsearch;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ShipElasticSearch {		
	
	private final static Logger LOGGER = Logger.getLogger(ShipElasticSearch.class);
	
    public SearchResponse getDocumentsByQuery(RestHighLevelClient client, String index, QueryBuilder query){    	
    	SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(1000); 
        searchSourceBuilder.query(query);
        request.source(searchSourceBuilder);
    	try {
    		SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);    		
    		return searchResponse;
    	} catch (ElasticsearchException e) {
            LOGGER.error(e.getDetailedMessage());
        } catch (IOException ex) {
            LOGGER.error(ex.getLocalizedMessage());
        }
		return null;
        
    }    

}
