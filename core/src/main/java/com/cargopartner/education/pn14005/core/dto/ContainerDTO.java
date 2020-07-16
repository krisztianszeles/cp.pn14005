package com.cargopartner.education.pn14005.core.dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContainerDTO implements Serializable{
	private static final long serialVersionUID =1L;
	private Long id;	
	@Size(min = 3, max = 255, message = "Container Name should be at least 3 letters long and not longer than 255")
    @NotNull(message = "Container name cannot be empty")
    private String name;
    private String creationDate;
    private String modificationDate;
    private String origin;
    private String destination;
    @Min(value = 0, message = "Weight cannot be negative")
    private Double weight;
    @Min(value = 0, message = "Volume cannot be negative")
    private Double volume;
    private ContainerStatus status;
    private ContainerType type;
    private Long shipID;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;		
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getModificationDate() {
		return modificationDate;
	}
	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getVolume() {
		return volume;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public ContainerStatus getStatus() {
		return status;
	}
	public void setStatus(ContainerStatus status) {
		this.status = status;
	}
	public Long getShipID() {
		return shipID;
	}
	public void setShipID(Long shipID) {
		this.shipID = shipID;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ContainerType getType() {
		return type;
	}
	public void setType(ContainerType type) {
		this.type = type;
	}   
        
	
        
}
