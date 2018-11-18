package com.epam.multithreading.demo;

import com.epam.multithreading.director.PortDirector;
import com.epam.multithreading.port.PortContentGenerator;

public class MultithreadingDemo {
    private static final String SHIPS_DATA_FILE = "src/test/resources/DemoShips.json";
    private static final int BERTH_AMOUNT = 3;
    private static final int STORAGE_CAPACITY = 20;

    public static void main(String[]args){
        initPort();
        PortDirector portDirector = new PortDirector();
        portDirector.process(SHIPS_DATA_FILE);
    }

    private static void initPort(){
        PortContentGenerator contentGenerator = new PortContentGenerator();
        contentGenerator.generatePort(BERTH_AMOUNT,STORAGE_CAPACITY);
    }
}
