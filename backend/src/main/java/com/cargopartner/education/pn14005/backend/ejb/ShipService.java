package com.cargopartner.education.pn14005.backend.ejb;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.elasticsearch.index.query.QueryBuilders;

import com.cargopartner.education.pn14005.backend.elasticsearch.ElasticSearchClient;
import com.cargopartner.education.pn14005.backend.elasticsearch.ShipElasticSearch;
import com.cargopartner.education.pn14005.backend.entity.Ship;
import com.cargopartner.education.pn14005.backend.entity.ShipConverter;
import com.cargopartner.education.pn14005.backend.events.DataActionType;
import com.cargopartner.education.pn14005.backend.events.DataUpdatedEvent;
import com.cargopartner.education.pn14005.core.dto.ShipDTO;
import com.cargopartner.education.pn14005.core.dto.ShipDimensionsDTO;
import com.cargopartner.education.pn14005.core.dto.ShipIndexDTO;

@Stateless
public class ShipService {
	@Inject
	private ShipDAO shipDAO;

	@Inject
	private ShipConverter shipConverter;

	@Inject
	private ShipElasticSearch shipElasticSearch;	
	
	@Inject
    private ElasticSearchClient elasticSearchClient;
	
	@Inject
    private Event<DataUpdatedEvent> dataUpdatedEvent;
	
	private final static String ELASTIC_INDEX = "ships";

	public ShipDTO createShip(ShipDTO shipDTO) {    
		Ship ship = shipDAO.createShip(shipConverter.toShip(shipDTO));		
		ShipIndexDTO shipIndexDTO = shipConverter.toShipIndexDTO(ship);
		dataUpdatedEvent.fire(new DataUpdatedEvent(DataActionType.CREATE, ship.getId(), shipConverter.objectToJSON(shipIndexDTO)));
		return shipIndexDTO;
	}
	public ShipDTO getShip(Long shipId) { 
		return shipConverter.toShipDTO(shipDAO.findShipById(shipId));
	}         
	public ShipDTO updateShip(ShipDTO shipDTO) {
		Ship ship = shipDAO.findShipById(shipDTO.getId());
		if(ship == null) {
			return null;
		}
		shipConverter.toShip(shipDTO, ship);  
		ShipIndexDTO shipIndexDTO  = shipConverter.toShipIndexDTO(ship);
		dataUpdatedEvent.fire(new DataUpdatedEvent(DataActionType.UPDATE, ship.getId(), shipConverter.objectToJSON(shipIndexDTO)));
		return shipIndexDTO;    		    	
	}       
	public Long deleteShip(Long shipId) {
		Ship ship = shipDAO.findShipById(shipId);
		if(ship == null) {
			return null;
		}
		shipDAO.deleteShip(ship);
		dataUpdatedEvent.fire(new DataUpdatedEvent(DataActionType.DELETE, ship.getId(), null));
		return shipId;
	}  
	public Set<ShipDTO> getEmptyShips() {
		List<Ship> ships = shipDAO.findEmptyShips();
		return ships.stream().map(shipConverter::toShipDTO).collect(Collectors.toSet());
	}
	public Set<ShipIndexDTO> indexAllShips() {
		elasticSearchClient.deleteIndex(ELASTIC_INDEX);
		List<Ship> ships = shipDAO.findAllShips();	
		return ships.stream()
				.map(ship -> shipConverter.toShipIndexDTO(ship))
				.map(shipIndexDTO -> {
					dataUpdatedEvent.fire(new DataUpdatedEvent(DataActionType.CREATE, shipIndexDTO.getId(), shipConverter.objectToJSON(shipIndexDTO)));
					return shipIndexDTO;
				}).collect(Collectors.toSet());
	}

	public Set<ShipIndexDTO> getAllShips() {
		Set<ShipIndexDTO> set = new HashSet<>();
		shipElasticSearch
			.getDocumentsByQuery(elasticSearchClient.getClient(), ELASTIC_INDEX, QueryBuilders.matchAllQuery())
			.getHits()
			.forEach(hit -> set.add(shipConverter.getObjectFromJson(hit.getSourceAsString(), ShipIndexDTO.class)));
		return set;
	}
	
    public Set<ShipIndexDTO> getAllShipsFromDB(int offset, int limit) {
        List<Ship> ships = shipDAO.findAllShips(offset, limit);
        return ships.stream().map(shipConverter::toShipIndexDTO).collect(Collectors.toSet());
    }
    
	public ShipDimensionsDTO getShipDimensions(Long shipId) {
		Ship ship = shipDAO.findShipById(shipId);
        if (ship == null) {
            return null;
        }
        Double weight = getShipContainersWeight(ship);
        Double volume = getShipContainersVolume(ship);

        return new ShipDimensionsDTO(ship.getId(), ship.getName(), weight, volume);
	}
	
    private Double getShipContainersWeight(Ship ship) {
        return ship.getContainers().stream()
        		.map(c-> Optional.ofNullable(c.getWeight()).orElse(0d))
        		.mapToDouble(Double::doubleValue)
        		.sum();        	
     }

     private Double getShipContainersVolume(Ship ship) {
     	 return ship.getContainers().stream()
       		   .map(c -> Optional.ofNullable(c.getVolume()).orElse(0d))
       		   .mapToDouble(Double::doubleValue)
       		   .sum();
     }
    
    public Long countAllShipsFromDB() {
    	return shipDAO.countAllShips();
    }
}
