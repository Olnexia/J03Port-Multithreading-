package com.epam.multithreading.port;

import com.epam.multithreading.container.Container;
import com.epam.multithreading.container.ContainerRegistrator;
import com.epam.multithreading.port.berthpool.Berth;
import com.epam.multithreading.port.berthpool.BerthPool;
import java.util.LinkedList;

public class PortContentGenerator {
    private  Port port = Port.getInstance();

    public void generatePort(final int berthAmount, int storageCapacity){
        LinkedList<Berth> berthList = new LinkedList <Berth>(){
            {
                for (int id = 0; id < berthAmount; id++) {
                    this.add(new Berth(id));
                }
            }
        };
        BerthPool berthPool = new BerthPool(berthList);
        port.setBerthPool(berthPool);
        generateHalfFullStorage(storageCapacity);
    }

    private void generateHalfFullStorage(int capacity){
        ContainerRegistrator containerRegistrator = ContainerRegistrator.getRegistrator();
        int containerAmount = capacity / 2;
        for(int i = 0; i< containerAmount; i++) {
            Container container = containerRegistrator.getContainer();
            port.offerContainer(container);
        }
    }

}
