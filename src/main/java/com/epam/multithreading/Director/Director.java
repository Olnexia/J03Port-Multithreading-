package com.epam.multithreading.Director;

import com.epam.multithreading.exception.ShipCreatingException;
import com.epam.multithreading.port.Port;
import com.epam.multithreading.ship.Ship;
import com.epam.multithreading.shipcreator.json.JsonShipCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Director {
    private static final Logger logger = LogManager.getLogger(Director.class);
    private static final String SHIPS_DATA_FILE = "src/test/resources/Ships.json";
    private static final int BERTH_AMOUNT = 2;
    private static final int STORAGE_CAPACITY = 20;

    public static void main(String[]args){
        Port port = Port.getInstance();
        port.generatePort(BERTH_AMOUNT,STORAGE_CAPACITY);
        ExecutorService executorService = Executors.newFixedThreadPool(BERTH_AMOUNT);
        List<Ship> ships;
        JsonShipCreator shipCreator = new JsonShipCreator();
        try{
            ships = shipCreator.createShips(SHIPS_DATA_FILE);
        } catch (ShipCreatingException e){
            logger.fatal("An error occurred while creating ships ",e);
            return;
        }
        setPort(ships,port);
        executeAll(executorService,ships);
        executorService.shutdown();
    }

    private static void setPort(List<Ship> ships,Port port){
        for (Ship ship :ships){
            ship.setPort(port);
        }
    }

    private static void executeAll(ExecutorService executorService,List<? extends Runnable> runnableList){
        for(Runnable task:runnableList){
            executorService.execute(task);
        }
    }
}
