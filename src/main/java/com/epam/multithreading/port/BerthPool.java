package com.epam.multithreading.port;

import com.epam.multithreading.exception.ResourceException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BerthPool {
    private Semaphore semaphore;
    private Queue<Berth> pool = new LinkedList<>();
    private Lock lock = new ReentrantLock();

    public BerthPool(Queue<Berth> pool){
        this.pool.addAll(pool);
        semaphore = new Semaphore(pool.size(),true);
    }

    public Berth getBerth() throws ResourceException {
        lock.lock();
        try {
            semaphore.acquire();
            Berth berth = pool.poll();
            if(berth!=null){
                System.out.println("Berth # "+ berth.getBerthId() +" occupied");
            }
            return berth;
        } catch (InterruptedException e) {
            throw new ResourceException(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    public void returnBerth(Berth berth){
        pool.add(berth);
        semaphore.release();
    }
}
