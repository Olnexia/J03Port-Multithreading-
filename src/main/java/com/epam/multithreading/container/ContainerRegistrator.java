package com.epam.multithreading.container;

public class ContainerRegistrator {
    private static ContainerRegistrator instance = null;
    private int currentContainerId;

    private ContainerRegistrator(){
    }

    public static ContainerRegistrator getRegistrator(){
        if(instance==null){
            instance = new ContainerRegistrator();
        }
        return instance;
    }

    public Container getContainer(){
        return new Container( currentContainerId++);
    }
}
