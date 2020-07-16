package com.cargopartner.education.pn14005.backend.entity;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.cargopartner.education.pn14005.backend.visitors.ContainerNameVisitor;
import com.cargopartner.education.pn14005.backend.visitors.ContainerTypeVisitor;
import com.cargopartner.education.pn14005.core.dto.ContainerDTO;
import com.cargopartner.education.pn14005.core.dto.ContainerType;
import com.cargopartner.education.pn14005.core.dto.ShipDTO;
import com.cargopartner.education.pn14005.core.dto.ShipIndexDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShipConverter {

	private final static Logger LOGGER = Logger.getLogger(ShipConverter.class);
	

	public ShipDTO toShipDTO(Ship ship) { 
		if(ship == null) {
			return null;
		}
		ShipDTO shipDTO = new ShipDTO();
		toShipDTO(ship, shipDTO);	
		return shipDTO;
	}
	
	public ShipIndexDTO toShipIndexDTO(Ship ship) {
		ShipIndexDTO shipIndexDTO = new ShipIndexDTO();
		toShipDTO(ship, shipIndexDTO);	
		shipIndexDTO.setContainerCount(ship.getContainers().size());
		return shipIndexDTO;		
	}
	
	private void toShipDTO(Ship ship, ShipDTO shipDTO) {
		shipDTO.setId(ship.getId());
		shipDTO.setName(ship.getName());
		shipDTO.setCreationDate(String.valueOf(ship.getCreationDate()));
		shipDTO.setModificationDate(String.valueOf(ship.getModificationDate()));  	        
		ship.getContainers().stream()
		.forEach(container -> {
			ContainerDTO containerDTO = toContainerDTO(container);
			shipDTO.addContainer(containerDTO);  
		});
	}

	public Ship toShip(ShipDTO shipDTO, Ship ship) { 	    	
		ship.setName(shipDTO.getName());
		ship.setModificationDate(Timestamp.valueOf(LocalDateTime.now()));
		removeNotExistingContainers(shipDTO, ship); 
		Map<Long, Container> containerMap = ship
				.getContainers()
				.stream()
				.collect(Collectors.toMap(container -> container.getId(), container -> container));      	
		shipDTO.getContainers().stream().forEach(containerDTO ->{
			String updatedType = containerDTO.getType().toString().toLowerCase();
			if(containerDTO.getId() != null) {					
				String currentType = containerMap.get(containerDTO.getId()).getClass().getSimpleName().toLowerCase().replaceAll("container", "");	
				if(!updatedType.equals(currentType)) {					
					Container container = toContainer(containerDTO, updatedType, false);
					container.setShip(ship);
					ship.deleteContainer(containerMap.get(containerDTO.getId()));
					ship.addContainer(container); 					
				}
				toContainer(containerDTO, containerMap.get(containerDTO.getId()));           			
			}else{
				Container container = toContainer(containerDTO, updatedType, true);
				container.setShip(ship);
				ship.addContainer(container); 
			}
		});
		return ship;
	}

	public Ship toShip(ShipDTO shipDTO) { 
		if(shipDTO == null) {
			return null;
		}
		Ship ship = new Ship();
		ship.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
		return toShip(shipDTO, ship);
	}

	public String objectToJSON(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (IOException e) {
			LOGGER.error("Error converting to JSON", e);
		}
		return null;
	}	
	
	public <T> T getObjectFromJson(String json, Class<T> clazz) {
		try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("Error converting to/from JSON", e);
        }
        return null;
	}


	private Container toContainer(ContainerDTO containerDTO, Container container) { 
		container.setName(containerDTO.getName());
		container.setModificationDate(Timestamp.valueOf(LocalDateTime.now()));
		container.setOrigin(containerDTO.getOrigin());
		container.setDestination(containerDTO.getDestination());
		container.setWeight(containerDTO.getWeight());
		container.setVolume(containerDTO.getVolume());
		container.setStatus(containerDTO.getStatus());	
		return container;
	}

	private Container toContainer(ContainerDTO containerDTO, String type, boolean newContainer) { 
		if(containerDTO == null) {
			return null;
		}		
		Container container = createSubContainer(type);		
		if(!newContainer) {
			container.setCreationDate(Timestamp.valueOf(containerDTO.getCreationDate()));
		}
		return toContainer(containerDTO, container);
	}	
	
	private Container createSubContainer(String type) {		
		 return type.equals(ContainerType.STANDARD.toString().toLowerCase()) ? new StandardContainer() : new RefrigeratorContainer();
	}

	private ContainerDTO toContainerDTO(Container container) { 
		ContainerDTO containerDTO = new ContainerDTO();
		containerDTO.setId(container.getId());
		containerDTO.setName(container.getName());
		containerDTO.setCreationDate(String.valueOf(container.getCreationDate()));
		containerDTO.setModificationDate(String.valueOf(container.getModificationDate()));
		containerDTO.setOrigin(container.getOrigin());
		containerDTO.setDestination(container.getDestination());
		containerDTO.setWeight(container.getWeight());
		containerDTO.setVolume(container.getVolume());
		containerDTO.setStatus(container.getStatus());
		containerDTO.setShipID(container.getShip().getId()); 
        containerDTO.setType(ContainerType.convertToEnum(container.accept(new ContainerTypeVisitor())));
		return containerDTO;	    	  	
	}

	private void removeNotExistingContainers(ShipDTO shipDTO, Ship ship) {
		Set<Long> containerDTOIDs = shipDTO
				.getContainers()
				.stream()
				.map(ContainerDTO::getId)
				.collect(Collectors.toSet());   		
		ship.getContainers().removeIf((container -> !containerDTOIDs.contains(container.getId())));
	} 


}
