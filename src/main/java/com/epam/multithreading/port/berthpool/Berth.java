package com.epam.multithreading.port.berthpool;

import java.util.ArrayList;
import java.util.List;

public class Berth {
    private int berthId;
    private List<Integer> unloadedContainersId = new ArrayList<>();

    public Berth(int id){
        this.berthId = id;
    }

    public void notifyUnloaded(int unloadedId){
        unloadedContainersId.add(unloadedId);
    }

    public void clearUnloadedList(){
        unloadedContainersId.clear();
    }

    public int getBerthId() {
        return berthId;
    }

    public List <Integer> getUnloadedContainersId() {
        return unloadedContainersId;
    }
}
