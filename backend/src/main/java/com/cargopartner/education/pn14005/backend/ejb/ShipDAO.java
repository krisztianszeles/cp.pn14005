package com.cargopartner.education.pn14005.backend.ejb;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.cargopartner.education.pn14005.backend.entity.Ship;

public class ShipDAO {

    @PersistenceContext(unitName = "spot_db")
    private EntityManager entityManager;

    public Ship createShip(Ship ship) {
        entityManager.persist(ship);  
        entityManager.flush();
        return ship;
    }
    public Ship findShipById(long id) {
        return entityManager.find(Ship.class, id);
    }    
   
    public void deleteShip(Ship ship) {
		entityManager.remove(ship);		
	}
    
    public List<Ship> findEmptyShips() {
        Query query = entityManager.createNamedQuery(Ship.QUERY_FIND_EMPTY, Ship.class);
        return query.getResultList();
    }
    
    public List<Ship> findAllShips() {
		Query query = entityManager.createNamedQuery(Ship.QUERY_FIND_ALL, Ship.class);
        return query.getResultList();
	}
    
	public List<Ship> findAllShips(int offset, int limit) {
		Query query = entityManager.createNamedQuery(Ship.QUERY_FIND_ALL, Ship.class);
		query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
	}	
	
	public Long countAllShips() {
		Query query = entityManager.createNamedQuery(Ship.QUERY_COUNT_ALL);
        return (Long) query.getSingleResult();
	}
}
