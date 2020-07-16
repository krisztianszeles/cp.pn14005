package com.cargopartner.education.pn14005.core.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class ShipDTO implements Serializable{
	private static final long serialVersionUID =1L;
	private Long id;	
	@Size(min = 3, max = 255, message = "Ship Name should be at least 3 letters long and not longer than 255")
	@NotNull(message ="Ship name cannot be empty")
	private String name;
	private String creationDate;
	private String modificationDate;
	private Set<ContainerDTO> containers = new HashSet<ContainerDTO>();
	
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
	public Set<ContainerDTO> getContainers() {
		return containers;
	}
	public void setContainers(Set<ContainerDTO> containers) {
		this.containers = containers;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
    public void addContainer(ContainerDTO containerDTO) {
        this.containers.add(containerDTO);
        containerDTO.setShipID(this.getId());
    }
    
    public void deleteContainer(ContainerDTO containerDTO) {
        this.containers.remove(containerDTO);
        containerDTO.setShipID(null);
    }
			
}
