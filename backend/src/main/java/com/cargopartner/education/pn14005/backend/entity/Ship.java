package com.cargopartner.education.pn14005.backend.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ship")
@NamedQueries({
	@NamedQuery(name = Ship.QUERY_FIND_EMPTY,
            query = "SELECT s FROM Ship s WHERE NOT EXISTS(SELECT c.id FROM Container c WHERE c.ship.id = s.id)"),
    @NamedQuery(name = Ship.QUERY_FIND_ALL,
    		query = "SELECT s FROM Ship s ORDER BY s.id"),
    @NamedQuery(name = Ship.QUERY_COUNT_ALL,
    		query = "SELECT COUNT(s) FROM Ship s")})
	
public class Ship {
	public static final  String QUERY_FIND_EMPTY = "Ship.findEmpty";
	public static final String QUERY_FIND_ALL = "Ship.findAll";
	public static final String QUERY_COUNT_ALL = "Ship.countAll";
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Size(min = 3, max = 255, message = "Ship Name should be at least 3 letters long and not longer than 255")
    @NotNull(message = "Ship name cannot be empty")
    @Column(name = "name")
    private String name;
    @Column(name = "created_on")
    private Timestamp creationDate;
    @Column(name = "modified_on")
    private Timestamp modificationDate;
    @OneToMany(mappedBy = "ship", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Container> containers = new HashSet<Container>();

    public Ship() {
        creationDate = new Timestamp(System.currentTimeMillis());
    }

    public Ship(@NotNull(message = "Ship name cannot be empty") String name) {
        super();
        this.name = name;
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

    public Set<Container> getContainers() {
        return containers;
    }   
    
    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }
    
    public void addContainer(Container container) {
        this.containers.add(container);
        container.setShip(this);
    }

    public void deleteContainer(Container container) {
        this.containers.remove(container);
        container.setShip(null);
    }

}
