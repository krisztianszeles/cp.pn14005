package com.cargopartner.education.pn14005.core.dto;

public class ShipIndexDTO extends ShipDTO {
	 private static final long serialVersionUID = 1L;
	 private Integer containerCount;
	 
	public Integer getContainerCount() {
		return containerCount;
	}
	public void setContainerCount(Integer containerCount) {
		this.containerCount = containerCount;
	}
}
