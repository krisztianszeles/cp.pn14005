package com.cargopartner.education.pn14005.backend.restservice;

import java.util.Set;

import javax.ejb.EJB;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.cargopartner.education.pn14005.backend.ejb.ShipService;
import com.cargopartner.education.pn14005.core.dto.ShipDTO;
import com.cargopartner.education.pn14005.core.dto.ShipDimensionsDTO;
import com.cargopartner.education.pn14005.core.dto.ShipIndexDTO;
import com.cargopartner.education.pn14005.core.response.ResponseMessages;

@Path("/ships")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class Rest {

	private final static Logger LOGGER = Logger.getLogger(Rest.class);

	@EJB
	private ShipService shipService;

	@GET
	@Path("/{shipId}")
	public Response getShip(@PathParam("shipId") long shipId) {    	
		try {
			ShipDTO resultShipDTO = shipService.getShip(shipId);
			if (resultShipDTO == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(ResponseMessages.RESOURCE_NOT_FOUND).build();
			}
			return Response.status(Response.Status.OK).entity(resultShipDTO).build();
		} catch (Exception exception) {
			LOGGER.error("Error fetching ship!", exception);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	public Response createShip(ShipDTO shipDTO) {
		try {
			ShipDTO resultShipDTO = shipService.createShip(shipDTO);
			return Response.status(Response.Status.OK).entity(resultShipDTO).build();
		} catch (Exception exception) {
			LOGGER.error("Error creating ship!", exception);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PUT
	public Response updateShip(ShipDTO shipDTO) {
		try {
			ShipDTO resultShipDTO = shipService.updateShip(shipDTO);
			if (resultShipDTO == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(ResponseMessages.RESOURCE_NOT_FOUND).build();
			}
			return Response.status(Response.Status.OK).entity(resultShipDTO).build();
		} catch (Exception exception) {
			LOGGER.error("Error updating ship!", exception);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DELETE
	@Path("/{shipId}")
	public Response deleteShip(@PathParam("shipId") long shipId) {
		try {
			Long id = shipService.deleteShip(shipId);
			if (id == null) {
				return Response.status(Response.Status.NOT_FOUND).entity(ResponseMessages.RESOURCE_NOT_FOUND).build();
			}
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (Exception ex) {
			LOGGER.error("Error deleting ship!", ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/empty")
	public Response getEmptyShips() {
		try {
			Set<ShipDTO> emptyShips = shipService.getEmptyShips();
			return Response.status(Response.Status.OK).entity(emptyShips).build();
		} catch (Exception ex) {
			LOGGER.error("Error fetching empty ships!", ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/reindex")
	public Response reIndexShips() {
		try {
			Set<ShipIndexDTO> allShips = shipService.indexAllShips();
			return Response.status(Response.Status.OK).entity(allShips).build();
		}catch (Exception ex) {
			LOGGER.error("Error fetching ships!", ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/all")
	public Response getAllShips() {
		try {
			Set<ShipIndexDTO> allShips = shipService.getAllShips();
			return Response.status(Response.Status.OK).entity(allShips).build();
		}catch (Exception ex) {
			LOGGER.error("Error fetching ships!", ex);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
		}
	}
	
    @GET
    @Path("/allFromDB/{offset}/{limit}")
    public Response getAllShipsFromDB(@NotNull @PathParam("offset") Integer offset, @NotNull @PathParam("limit") Integer limit) {
        try {
            Set<ShipIndexDTO> result = shipService.getAllShipsFromDB(offset, limit);
            return Response.status(Response.Status.OK).entity(new GenericEntity<Set<ShipIndexDTO>>(result){}).build();
        } catch (Exception ex) {
            LOGGER.error("Error fetching ships from DB!", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GET
    @Path("/dimensions/{shipId}")
    public Response getShipDimensions(@PathParam("shipId") Long shipId) {
        try {
            ShipDimensionsDTO shipDimensions = shipService.getShipDimensions(shipId);
            if (shipDimensions == null) {
                return Response.status(Response.Status.NOT_FOUND).entity(ResponseMessages.RESOURCE_NOT_FOUND).build();
            }
            return Response.status(Response.Status.OK).entity(shipDimensions).build();
        } catch (Exception ex) {
            LOGGER.error("Error fetching ship dimensions!", ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GET
    @Path("/countAllFromDB")
    public Response countAllShipsFromDB() {
    	try {
    		Long numberOfShips = shipService.countAllShipsFromDB();
    		if(numberOfShips == null) {
    			return Response.status(Response.Status.NOT_FOUND).entity(ResponseMessages.RESOURCE_NOT_FOUND).build();
    		}
    		return Response.status(Response.Status.OK).entity(numberOfShips).build();
    	} catch (Exception ex) {
    		LOGGER.error("Error counting ships!", ex);
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ResponseMessages.INTERNAL_SERVER_ERROR).build();
    	}
    }
}





