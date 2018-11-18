package com.epam.multithreading.ship.creator;

import com.epam.multithreading.container.Container;
import com.epam.multithreading.container.ContainerRegistrator;
import com.epam.multithreading.exception.JsonParsingException;
import com.epam.multithreading.port.Port;
import com.epam.multithreading.ship.Ship;
import com.epam.multithreading.ship.parser.json.JsonShipParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ShipCreator {
    private static final ContainerRegistrator CONTAINER_REGISTRATOR = ContainerRegistrator.getRegistrator();
    private static final Logger logger = LogManager.getLogger(ShipCreator.class);

    public List<Ship> createShips(String shipsFilePath){
        JsonShipParser jsonParser = new JsonShipParser();
        List<Ship> ships;
        try{
            ships = jsonParser.parse(shipsFilePath);
        }catch (JsonParsingException e){
            logger.error(e.getMessage());
            return Collections.emptyList();
        }
        Port port = Port.getInstance();
        ships.forEach(ship->ship.setPort(port));
        ships.forEach(ship->generateStorage(ship,ship.getContainerAmount()));
        return ships;
    }

    private void generateStorage(Ship ship, int containerAmount) {
        Queue<Container> containers = new LinkedList <>();
        for (int i = 0; i < containerAmount; i++) {
            Container container = CONTAINER_REGISTRATOR.getContainer();
            containers.add(container);
        }
        ship.setContainers(containers);
    }
}
