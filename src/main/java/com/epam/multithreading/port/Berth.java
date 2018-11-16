package com.epam.multithreading.port;

import java.util.ArrayList;
import java.util.List;

public class Berth {
    private int berthId;
    private List<Integer> unloadedContainersId = new ArrayList<>();

    public Berth(int id){
        this.berthId = id;
    }

    public int getBerthId() {
        return berthId;
    }

    public void notifyUnloaded(int unloadedId){
        unloadedContainersId.add(unloadedId);
    }

    public void clearUnloadedList(){
        unloadedContainersId.clear();
    }

    public List <Integer> getUnloadedContainersId() {
        return unloadedContainersId;
    }
}
