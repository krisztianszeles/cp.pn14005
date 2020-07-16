package backend;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cargopartner.education.pn14005.backend.entity.Container;
import com.cargopartner.education.pn14005.backend.entity.Ship;
import com.cargopartner.education.pn14005.backend.entity.ShipConverter;
import com.cargopartner.education.pn14005.backend.entity.StandardContainer;
import com.cargopartner.education.pn14005.core.dto.ContainerDTO;
import com.cargopartner.education.pn14005.core.dto.ContainerStatus;
import com.cargopartner.education.pn14005.core.dto.ContainerType;
import com.cargopartner.education.pn14005.core.dto.ShipDTO;

public class ShipConverterTest {

	private ShipConverter shipconverter;

	@BeforeEach
	void initEach() {
		shipconverter = new ShipConverter();
	}

	@DisplayName("Testing toShipDTO(Ship ship)")
	@Test
	void tesToShipDTO() {
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
		String s = ContainerType.STANDARD.toString();
		ShipDTO shipDTO = shipconverter.toShipDTO(ship);


		assertAll(
				() -> assertEquals(ship.getId(), shipDTO.getId(), "Ship.id is not equal"),
				() -> assertEquals(ship.getName(), shipDTO.getName(), "Ship.name is not equal"),
				() -> assertEquals(ship.getCreationDate().toString(), shipDTO.getCreationDate().toString(), "Ship.creationDate is not equal"),
				() -> assertEquals(ship.getModificationDate().toString(), shipDTO.getModificationDate().toString(), "Ship.modificationDate is not equal")
				);
		
		ship.getContainers().forEach(container -> shipDTO.getContainers().stream().forEach(containerDTO -> {
			assertAll(
					() -> assertEquals(container.getId(), containerDTO.getId(), "Container.id is not equal"),
					() -> assertEquals(container.getName(), containerDTO.getName(), "Container.name is not equal"),
					() -> assertEquals(container.getCreationDate().toString(), containerDTO.getCreationDate().toString(), "Container.creationDate is not equal"),
					() -> assertEquals(container.getModificationDate().toString(), containerDTO.getModificationDate().toString(), "Container.modificationDate is not equal"),
					() -> assertEquals(container.getOrigin(), containerDTO.getOrigin(), "Container.origin is not equal"),
					() -> assertEquals(container.getDestination(), containerDTO.getDestination(), "Container.destination is not equal"),
					() -> assertEquals(container.getStatus(), containerDTO.getStatus(), "Container.status is not equal"),
					() -> assertEquals(container.getWeight(), containerDTO.getWeight(), "Container.weight is not equal"),
					() -> assertEquals(container.getVolume(), containerDTO.getVolume(), "Container.volume is not equal"),
					() -> assertEquals(container.getShip().getId(), containerDTO.getShipID(), "Container.shipId is not equal"),
					() -> assertEquals(container.getClass().getSimpleName().toLowerCase().replaceAll("container", ""), containerDTO.getType().toString().toLowerCase(), "Container.id is not equal")
					);
		}));
	}
}
