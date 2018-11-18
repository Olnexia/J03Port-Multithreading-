package com.epam.multithreading.director;

import com.epam.multithreading.ship.Ship;
import com.epam.multithreading.ship.creator.ShipCreator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortDirector {
    private static final int CONCURRENT_AMOUNT = 3;

    public void process(String shipsPath){
        ShipCreator shipCreator = new ShipCreator();
        List<Ship> ships = shipCreator.createShips(shipsPath);

        ExecutorService executorService = Executors.newFixedThreadPool(CONCURRENT_AMOUNT);
        ships.forEach(executorService::execute);
        executorService.shutdown();
    }
}
