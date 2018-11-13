package com.epam.multithreading.ship;

import com.epam.multithreading.entity.Container;
import com.epam.multithreading.port.Berth;
import com.epam.multithreading.port.Port;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Ship implements Runnable {
    private static final Logger logger = LogManager.getLogger(Ship.class);
    private static int currentContainerId = 100;
    private static Random random = new Random();
    private String name;
    private Port port;
    private Berth berth;
    private Queue<Container> containers = new LinkedList <>();
    private int containerAmount;
    private int capacity;

    public Ship(String name, int capacity){
        this.name = name;
        this.capacity = capacity;
        generateStorage(capacity);
    }

    public void run() {
        moor();
        if(containerAmount == 0){
            loadShip();
        } else{
            unloadShip();
            loadShip();
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
            if(port.setContainer(container)){
                berth.notifyUnloaded(container.getRegistrationNumber());
                System.out.println("The ship " + name + " has unloaded container # "+ container.getRegistrationNumber());
            } else{
                System.out.println("The port storage is full");
                return;
            }
            containerAmount--;
        }
    }

    private void loadShip(){
        while(containerAmount<capacity){
            Container container = port.getContainer();
            if(container == null){
                System.out.println("The port storage is empty");
                return;
            }
            List<Integer> unloadedContainersId = berth.getUnloadedContainersId();
            if(unloadedContainersId.contains(container.getRegistrationNumber())){
                port.setContainer(container);
                continue;
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
            TimeUnit.MILLISECONDS.sleep(100);
            System.out.println("The Ship " + name +" has moored to the berth # "+ berth.getBerthId());
        }catch (InterruptedException e){
            logger.error("An error occurred while mooring ship " + name,e.getMessage());
        }
    }

    private void leave(){
        try {
            port.returnBerth(berth);
            System.out.println("The ship " + name + " has leaved the berth # " + berth.getBerthId());
            TimeUnit.MILLISECONDS.sleep(100);
        }catch (InterruptedException e){
            logger.error("An error occurred while leaving the port ",e.getMessage());
        }
    }

    private void generateStorage(int capacity) {
        containerAmount = random.nextInt(capacity);
        for (int i = 0; i < containerAmount; i++) {
            containers.add(new Container(currentContainerId++));
        }
    }

    public void setPort(Port port) {
        this.port = port;
    }
}
