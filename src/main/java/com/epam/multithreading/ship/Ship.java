package com.epam.multithreading.ship;

import com.epam.multithreading.container.Container;
import com.epam.multithreading.exception.ResourceException;
import com.epam.multithreading.port.berthpool.Berth;
import com.epam.multithreading.port.Port;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Ship implements Runnable {
    private static final Logger logger = LogManager.getLogger(Ship.class);

    private String name;
    private Port port;
    private Berth berth;
    private int capacity;
    private int containerAmount;
    private RequestedPortService requestedPortService;
    private Queue<Container> containers = new LinkedList <>();

    @JsonCreator
    public Ship(@JsonProperty("name") String name,@JsonProperty("capacity") int capacity,
                @JsonProperty("container amount") int containerAmount,
                @JsonProperty("requested service")RequestedPortService requestedPortService){
        this.name = name;
        this.capacity = capacity;
        this.requestedPortService = requestedPortService;
        this.containerAmount = containerAmount;
    }

    public void run() {
        moor();
        switch (requestedPortService){
            case UNLOAD:
                unloadShip();
                break;
            case LOAD:
                loadShip();
                break;
            case UNLOAD_LOAD:
                unloadShip();
                loadShip();
                break;
                default:
                    logger.error("Illegal port activity value. Ship "+name);
                    throw new IllegalArgumentException();
        }
        leave();
    }

    private void unloadShip(){
        while(containerAmount>0){
            Container container = containers.poll();
            if(container==null){
                logger.info("The ship" + name + "is empty");
                System.out.println("The ship" + name + "is empty");
                break;
            }
            if(port.offerContainer(container)){
                int containerId = container.getRegistrationNumber();
                berth.notifyUnloaded(containerId);
                logger.info("The ship " + name + " has unloaded container # "+ containerId);
                System.out.println("The ship " + name + " has unloaded container # "+ containerId);
            } else{
                logger.info("The port storage is full");
                System.out.println("The port storage is full");
                break;
            }
            containerAmount--;
        }
    }

    private void loadShip(){
        List<Integer> unloadedContainersId = berth.getUnloadedContainersId();
        while(containerAmount<capacity){
            Container container = port.getNewContainer(unloadedContainersId);
            if(container == null){
                logger.info("Ship "+name+": Nothing to load");
                System.out.println("Ship "+name+": Nothing to load");
                break;
            }
            if(containers.offer(container)){
                containerAmount++;
                int containerId = container.getRegistrationNumber();
                logger.info("The ship " + name + " has loaded container # "+containerId);
                System.out.println("The ship " + name + " has loaded container # "+containerId);
            } else{
                logger.info("The storage of ship "+name+" is full");
                System.out.println("The storage of ship "+name+" is full");
            }
        }
    }

    private void moor(){
        try{
            berth = port.getBerth();
            TimeUnit.MICROSECONDS.sleep(100);
            System.out.println("The Ship " + name +" has moored to the berth # "+ berth.getBerthId());
        }catch (InterruptedException| ResourceException e){
            logger.error("An error occurred while mooring ship " + name,e.getMessage());
        }
    }

    private void leave(){
        try {
            berth.clearUnloadedList();
            port.returnBerth(berth);
            System.out.println("The ship " + name + " has left the berth # " + berth.getBerthId());
            TimeUnit.MICROSECONDS.sleep(100);
        }catch (InterruptedException e){
            logger.error("An error occurred while leaving the port ",e.getMessage());
        }
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public void setContainers(Queue <Container> containers) {
        this.containers = containers;
    }

    public RequestedPortService getRequestedPortService() {
        return requestedPortService;
    }

    public int getContainerAmount() {
        return containerAmount;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getName() {
        return name;
    }
}
