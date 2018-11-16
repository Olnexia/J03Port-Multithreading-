package com.epam.multithreading.ship;

import com.epam.multithreading.container.Container;
import com.epam.multithreading.container.ContainerRegistrar;
import com.epam.multithreading.port.Berth;
import com.epam.multithreading.port.Port;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Ship implements Runnable {
    private static final ContainerRegistrar CONTAINER_REGISTRAR = ContainerRegistrar.getRegistrar();
    private static final Logger logger = LogManager.getLogger(Ship.class);
    private String name;
    private Port port;
    private Berth berth;
    private Target target;
    private Queue<Container> containers = new LinkedList <>();
    private int containerAmount;
    private int capacity;

    public Ship(String name, int capacity, int containerAmount,Target target){
        this.name = name;
        this.capacity = capacity;
        this.target = target;
        this.containerAmount = containerAmount;
        if(containerAmount>0){
            generateStorage(containerAmount);
        }
    }

    public void run() {
        moor();
        switch (target){
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
        }
        leave();
    }

    private void unloadShip(){

        while(containerAmount>0){
            Container container = containers.poll();
            if(container==null){
                System.out.println("The ship" + name + "is empty");
                return;
            }
            if(port.offerContainer(container)){
                int containerId = container.getRegistrationNumber();
                berth.notifyUnloaded(containerId);
                System.out.println("The ship " + name + " has unloaded container # "+ container.getRegistrationNumber());
            } else{
                System.out.println("The port storage is full");
                return;
            }
            containerAmount--;
        }
    }

    private void loadShip(){
        List<Integer> unloadedContainersId = berth.getUnloadedContainersId();
        while(containerAmount<capacity){
            Container container = port.getNewContainer(unloadedContainersId);
            if(container == null){
                System.out.println("Ship "+name+": Nothing to load");
                return;
            }
            if(containers.offer(container)){
                containerAmount++;
                System.out.println("The ship " + name + " has loaded container # "+container.getRegistrationNumber());
            } else{
                System.out.println("The storage of ship "+name+" is full");
            }
        }
    }

    private void moor(){
        try{
            berth = port.getBerth();
            TimeUnit.MICROSECONDS.sleep(100);
            System.out.println("The Ship " + name +" has moored to the berth # "+ berth.getBerthId());
        }catch (InterruptedException e){
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

    private void generateStorage(int containerAmount) {
        for (int i = 0; i < containerAmount; i++) {
            Container container = CONTAINER_REGISTRAR.getContainer();
            containers.add(container);
        }
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public Target getTarget() {
        return target;
    }

    public int getContainerAmount() {
        return containerAmount;
    }

    public int getCapacity() {
        return capacity;
    }
}
