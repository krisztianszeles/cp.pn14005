package com.cargopartner.education.pn14005.core.dto;

public class ShipDimensionsDTO {
	 private Long id;
	    private String name;
	    private Double weight;
	    private Double volume;

	    public ShipDimensionsDTO() {
	    }

	    public ShipDimensionsDTO(Long id, String name, Double weight, Double volume) {
	        this.id = id;
	        this.name = name;
	        this.weight = weight;
	        this.volume = volume;
	    }

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
}
