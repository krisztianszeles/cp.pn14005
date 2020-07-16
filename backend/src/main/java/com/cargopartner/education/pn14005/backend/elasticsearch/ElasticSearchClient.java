package com.cargopartner.education.pn14005.backend.elasticsearch;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

public class ElasticSearchClient {
	
	private final static Logger LOGGER = Logger.getLogger(ElasticSearchClient.class);
	private RestHighLevelClient client;
	
	public RestHighLevelClient getClient() {
        if (client == null) {
            client = new RestHighLevelClient(RestClient.builder(
                            new HttpHost("localhost", 9200, "http"),
                            new HttpHost("localhost", 9201, "http")));
        }
        return client;
    }
	
    public void createDocument(String index, Long id, String jsonString) {
        IndexRequest request = new IndexRequest(index);
        request.id(id.toString());
        request.source(jsonString, XContentType.JSON);
        try {
            getClient().index(request, RequestOptions.DEFAULT);            
        } catch (ElasticsearchException e) {
            LOGGER.error(e.getDetailedMessage());
        } catch (IOException ex) {
            LOGGER.error(ex.getLocalizedMessage());
        }
    }
    
    public void updateDocument(String index, Long id, String jsonString) {
        UpdateRequest request = new UpdateRequest();
        request.index(index);
        request.id(id.toString());
        request.doc(jsonString, XContentType.JSON);

        try {
            getClient().update(request, RequestOptions.DEFAULT);           
        } catch (ElasticsearchException e) {
            LOGGER.error(e.getDetailedMessage());
        } catch (IOException ex) {
            LOGGER.error(ex.getLocalizedMessage());
        }
    }
    
    public void deleteDocument(String index, Long id) {
        DeleteRequest request = new DeleteRequest();
        request.index(index);
        request.id(id.toString());
        try {
            getClient().delete(request, RequestOptions.DEFAULT);                     
        } catch (ElasticsearchException e) {
            LOGGER.error(e.getDetailedMessage());
        } catch (IOException ex) {
            LOGGER.error(ex.getLocalizedMessage());
        }
    }
    
    public void deleteIndex(String index) {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        try {
            getClient().indices().delete(request, RequestOptions.DEFAULT);                     
        } catch (ElasticsearchException e) {
            LOGGER.error(e.getDetailedMessage());
        } catch (IOException ex) {
            LOGGER.error(ex.getLocalizedMessage());
        }
    }    
}
