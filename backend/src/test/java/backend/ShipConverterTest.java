package backend;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cargopartner.education.pn14005.backend.converters.ShipConverter;
import com.cargopartner.education.pn14005.backend.entity.Container;
import com.cargopartner.education.pn14005.backend.entity.Ship;
import com.cargopartner.education.pn14005.backend.entity.StandardContainer;
import com.cargopartner.education.pn14005.core.dto.ContainerDTO;
import com.cargopartner.education.pn14005.core.dto.ContainerStatus;
import com.cargopartner.education.pn14005.core.dto.ContainerType;
import com.cargopartner.education.pn14005.core.dto.ShipDTO;
import com.cargopartner.education.pn14005.core.dto.ShipIndexDTO;

public class ShipConverterTest {

	private ShipConverter shipconverter;

	@BeforeEach
	void initEach() {
		shipconverter = new ShipConverter();
	}

	@DisplayName("Testing Ship -> ShipDTO converter")
	@Test
	void testShipToDtoConverter() {
		Ship ship = createShip();	

		ShipDTO shipDTO = shipconverter.toShipDTO(ship);

		assertAll(
				() -> assertEquals(ship.getId(), shipDTO.getId(), "Ship.id is not equal"),
				() -> assertEquals(ship.getName(), shipDTO.getName(), "Ship.name is not equal")
				);
	}

	@DisplayName("Testing Container and ContainerDTO type after conversion")
	@Test
	void testContainerType() {
		Ship ship = createShip();	

		ShipDTO shipDTO = shipconverter.toShipDTO(ship);
		
		ship.getContainers()
			.forEach(container -> shipDTO.getContainers().stream()
			.forEach(containerDTO -> {
				assertEquals(container.getClass().getSimpleName().toLowerCase().replaceAll("container", ""), containerDTO.getType().toString().toLowerCase(), "Container.type is not equal");
				}));
	}
	
	@DisplayName("Testing Container count in ShipIndexDTO")
	@Test
	void testContainerCountSet() {
		Ship ship = createShip();	
		
		ShipIndexDTO shipIndexDTO = shipconverter.toShipIndexDTO(ship);
		
		assertEquals(ship.getContainers().size(), shipIndexDTO.getContainerCount(), "Container count is incorrect");
	}
	
	@DisplayName("Testing ShipDTO -> Ship converter")
	@Test
	void testShipDtoToShipConverter() {
		ShipDTO shipDTO = createShipDTO();	

		Ship ship = shipconverter.toShip(shipDTO);

		assertEquals(ship.getName(), shipDTO.getName(), "Ship.name is not equal");
	}

	private Ship createShip() {
		Ship ship = new Ship();
		ship.setId(1L);
		ship.setName("Big Ship");
		ship.setCreationDate(new Timestamp(System.currentTimeMillis()));
		ship.setModificationDate(new Timestamp(System.currentTimeMillis()));
		Container container1 = new StandardContainer();
		container1.setId(1L);
		container1.setName("First Container");
		container1.setCreationDate(new Timestamp(System.currentTimeMillis()));
		container1.setModificationDate(new Timestamp(System.currentTimeMillis()));
		container1.setOrigin("TOKYO");
		container1.setDestination("RIO");
		container1.setStatus(ContainerStatus.DEPARTED);
		container1.setWeight(12000d);
		container1.setVolume(67d);
		container1.setShip(ship);
		ship.addContainer(container1);	
		return ship;
	}
	
	private ShipDTO createShipDTO() {
		ShipDTO shipDTO = new ShipDTO();
		shipDTO.setId(1L);
		shipDTO.setName("Big Ship");
		shipDTO.setCreationDate(new Timestamp(System.currentTimeMillis()).toString());
		shipDTO.setModificationDate(new Timestamp(System.currentTimeMillis()).toString());
		ContainerDTO containerDTO1 = new ContainerDTO();
		containerDTO1.setId(null);
		containerDTO1.setName("First Container");
		containerDTO1.setCreationDate(new Timestamp(System.currentTimeMillis()).toString());
		containerDTO1.setModificationDate(new Timestamp(System.currentTimeMillis()).toString());
		containerDTO1.setOrigin("TOKYO");
		containerDTO1.setDestination("RIO");
		containerDTO1.setStatus(ContainerStatus.DEPARTED);
		containerDTO1.setWeight(12000d);
		containerDTO1.setVolume(67d);
		containerDTO1.setShipID(shipDTO.getId());
		containerDTO1.setType(ContainerType.REFRIGERATOR);
		shipDTO.addContainer(containerDTO1);	
		return shipDTO;
	}
	
}
