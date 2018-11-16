package com.epam.multithreading.port;

import com.epam.multithreading.exception.ResourceException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BerthPool {
    private Semaphore semaphore;
    private Queue<Berth> pool = new LinkedList<>();

    public BerthPool(Queue<Berth> pool){
        this.pool.addAll(pool);
        semaphore = new Semaphore(pool.size(),true);
    }

    public Berth getBerth() throws ResourceException {
        try {
            semaphore.acquire();
            Berth berth = pool.poll();
            if(berth == null){
                throw new ResourceException("A berth was not received");
            }
            System.out.println("Berth # "+ berth.getBerthId() +" occupied");
            return berth;
        } catch (InterruptedException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }

    public void returnBerth(Berth berth){
        pool.add(berth);
        semaphore.release();
    }
}
