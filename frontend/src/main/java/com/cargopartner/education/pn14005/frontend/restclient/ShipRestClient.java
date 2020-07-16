package com.cargopartner.education.pn14005.frontend.restclient;

import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import com.cargopartner.education.pn14005.core.dto.ShipDTO;
import com.cargopartner.education.pn14005.core.dto.ShipDimensionsDTO;
import com.cargopartner.education.pn14005.core.dto.ShipIndexDTO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;


public class ShipRestClient {

	private final static Logger LOGGER = Logger.getLogger(ShipRestClient.class);
	private final static String REST_URI = "http://localhost:8080/backend-1.0-SNAPSHOT/rest/ships";

	private WebResource.Builder getWebResourceBuilder(String appendix) {
		ClientConfig config = new DefaultClientConfig();
		config.getClasses().add(JacksonJaxbJsonProvider.class);
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(config);

		return client
				.resource(REST_URI + appendix)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON);
	}

	public ShipDTO getShip(Long shipId) {
		ClientResponse response = getWebResourceBuilder("/" + shipId).get(ClientResponse.class);
		verifyResponse(response, "get ship");
		return response.getEntity(ShipDTO.class);
	}

	public ShipIndexDTO createShip(ShipDTO shipDTO) {
		ClientResponse response = getWebResourceBuilder("").post(ClientResponse.class, shipDTO);
		verifyResponse(response, "create ship");
		return response.getEntity(ShipIndexDTO.class);
	}

	public ShipIndexDTO updateShip(ShipDTO shipDTO) {
		ClientResponse response = getWebResourceBuilder("").put(ClientResponse.class, shipDTO);
        verifyResponse(response, "update ship");
        return response.getEntity(ShipIndexDTO.class);
	}
	
	public void deleteShip(Long shipId) {
		ClientResponse response = getWebResourceBuilder("/" + shipId).delete(ClientResponse.class);
        verifyResponse(response, "delete ship");
	}

	public Set<ShipIndexDTO> getAllShipsFromDB(int offset, int limit) {
		ClientResponse response = getWebResourceBuilder("/allFromDB" + "/" + offset + "/" + limit).get(ClientResponse.class);
		verifyResponse(response, "get all ships from DB");
		return response.getEntity(new GenericType<Set<ShipIndexDTO>>() {
		});
	}
	
	public Set<ShipIndexDTO> getAllShipsFromElastic() {
		ClientResponse response = getWebResourceBuilder("/all").get(ClientResponse.class);
		verifyResponse(response, "get all ships from Elasticsearch");
		return response.getEntity(new GenericType<Set<ShipIndexDTO>>() {
		});
	}

	public ShipDimensionsDTO getShipDimensions(long shipId) {
		ClientResponse response = getWebResourceBuilder("/dimensions" + "/" + shipId).get(ClientResponse.class);
		verifyResponse(response, "get ship dimensions");
		return response.getEntity(ShipDimensionsDTO.class);
	}
	
	public Long countAllShips() {
		ClientResponse response = getWebResourceBuilder("/countAllFromDB").get(ClientResponse.class);
		verifyResponse(response, "get ship dimensions");
		return response.getEntity(Long.class);
	}

	private void verifyResponse(ClientResponse response, String action) {
		if (response.getStatus() < 200 || response.getStatus() >= 300) {
			String message = "Action " + action + ", HTTP error code: " + response.getStatus();
			if (LOGGER.isEnabledFor(Level.ERROR)) {
				LOGGER.error(message);
			}
			throw new RuntimeException(message);
		}
	}
}
