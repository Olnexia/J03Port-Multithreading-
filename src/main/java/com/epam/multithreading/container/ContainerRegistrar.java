package com.epam.multithreading.container;

public class ContainerRegistrar {
    private static ContainerRegistrar instance = null;
    private int currentContainerId;

    private ContainerRegistrar(){
    }

    public static ContainerRegistrar getRegistrar(){
        if(instance==null){
            instance = new ContainerRegistrar();
        }
        return instance;
    }

    public Container getContainer(){
        return new Container( currentContainerId++);
    }
}
