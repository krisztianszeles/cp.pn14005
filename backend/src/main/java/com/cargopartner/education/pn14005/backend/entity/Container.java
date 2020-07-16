package com.cargopartner.education.pn14005.backend.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cargopartner.education.pn14005.backend.visitors.ContainerVisitor;
import com.cargopartner.education.pn14005.backend.visitors.Visitable;
import com.cargopartner.education.pn14005.core.dto.ContainerStatus;

@Entity
@Table(name = "container")
@DiscriminatorColumn(name = "container_type")
public abstract class Container implements Visitable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Size(min = 3, max = 255, message = "Container Name should be at least 3 letters long and not longer than 255")
    @NotNull(message = "Container name cannot be empty")
    @Column(name = "name")
    private String name;
    @Column(name = "created_on")
    private Timestamp creationDate;
    @Column(name = "modified_on")
    private Timestamp modificationDate;
    @Column(name = "origin")
    private String origin;
    @Column(name = "destination")
    private String destination;
    @Min(value = 0, message = "Weight cannot be negative")
    @Column(name = "weight")
    private Double weight;
    @Min(value = 0, message = "Volume cannot be negative")
    @Column(name = "volume")
    private Double volume;
    @Enumerated(EnumType.STRING)
    private ContainerStatus status;
    @ManyToOne
    @JoinColumn(name = "ship_id", referencedColumnName = "id")
    private Ship ship;

    public Container() {
        this.creationDate = new Timestamp(System.currentTimeMillis());
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

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Timestamp modificationDate) {
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

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public abstract String accept(ContainerVisitor visitor);
    
}
