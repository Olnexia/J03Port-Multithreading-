package com.epam.multithreading;

import com.epam.multithreading.Exception.ResourceException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Pool<T> {
    private Semaphore semaphore;
    private Queue<T> pool = new LinkedList<T>();

    public Pool(Queue<T> pool, int poolSize){
        this.pool.addAll(pool);
        semaphore = new Semaphore(poolSize,true);
    }

    public T getResource() throws ResourceException {
        try{
            semaphore.acquire();
            return pool.poll();

        } catch (InterruptedException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }

    public void returnResource(T resource){
        pool.add(resource);
        semaphore.release();
    }
}
