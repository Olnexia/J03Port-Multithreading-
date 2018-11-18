 package com.epam.multithreading.port;

 import com.epam.multithreading.exception.ResourceException;
 import com.epam.multithreading.container.Container;
 import com.epam.multithreading.port.berthpool.Berth;
 import com.epam.multithreading.port.berthpool.BerthPool;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Queue;
 import java.util.concurrent.atomic.AtomicBoolean;
 import java.util.concurrent.locks.Lock;
 import java.util.concurrent.locks.ReentrantLock;

 public class Port {
     private static AtomicBoolean initialized = new AtomicBoolean(false);
     private static Port instance = null;
     private static Lock lock = new ReentrantLock();

     private BerthPool berthPool;
     private Queue<Container> containers = new LinkedList <>();

     private Port() {
     }

     public static Port getInstance() {
         if(!initialized.get()) {
             try {
                 lock.lock();
                 if (!initialized.get()) {
                     instance = new Port();
                     initialized.set(true);
                 }
             } finally {
                 lock.unlock();
             }
         }
         return instance;
     }

     public Berth getBerth()throws ResourceException{
         try {
             return berthPool.getBerth();
         }catch (ResourceException e){
             throw new ResourceException(e.getMessage());
         }
     }

     public void returnBerth(Berth berth){
         berthPool.returnBerth(berth);
     }

     public boolean offerContainer(Container container){
         lock.lock();
         try{
             return containers.offer(container);
         }finally {
             lock.unlock();
         }
     }

     public Container getNewContainer(List<Integer> oldContainersId){
         lock.lock();
         try{
             for(Container container:containers){
                 int containerId = container.getRegistrationNumber();
                 if(!oldContainersId.contains(containerId)){
                    containers.remove(container);
                    return container;
                 }
             }
             return null;
         }finally {
             lock.unlock();
         }
     }

     public void setBerthPool(BerthPool berthPool) {
         this.berthPool = berthPool;
     }
 }
