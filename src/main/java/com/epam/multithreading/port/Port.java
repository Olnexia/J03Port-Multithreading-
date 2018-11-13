 package com.epam.multithreading.port;

 import com.epam.multithreading.exception.ResourceException;
 import com.epam.multithreading.entity.Container;
 import org.apache.logging.log4j.LogManager;
 import org.apache.logging.log4j.Logger;
 import java.util.LinkedList;
 import java.util.Queue;
 import java.util.concurrent.atomic.AtomicBoolean;
 import java.util.concurrent.locks.Lock;
 import java.util.concurrent.locks.ReentrantLock;

 public class Port {
     private static final Logger logger = LogManager.getLogger(Port.class);
     private static int currentContainerId;
     private static AtomicBoolean initialized = new AtomicBoolean(false);
     private static Port instance = null;
     private static Lock lock = new ReentrantLock();
     private BerthPool berthPool;
     private Queue<Container> containers = new LinkedList <>();
     private int containerAmount;

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

     public void generatePort(final int berthAmount, int storageCapacity){
         LinkedList<Berth> berthList = new LinkedList <Berth>(){
             {
                 for (int id = 0; id < berthAmount; id++) {
                     this.add(new Berth(id));
                 }
             }
         };
         BerthPool berthPool = new BerthPool(berthList);
         setBerthPool(berthPool);
         generateHalfFullStorage(storageCapacity);
     }

     private void generateHalfFullStorage(int capacity){
         lock.lock();
         try{
             containerAmount = capacity/2;
             for(int i = 0; i<containerAmount;i++){
                 containers.add(new Container(currentContainerId++));
             }
         }finally {
             lock.unlock();
         }
     }

     public void setBerthPool(BerthPool berthPool) {
         this.berthPool = berthPool;
     }

     public BerthPool getBerthPool() {
         return berthPool;
     }

     public Berth getBerth(){
         Berth berth = null;
         try {
             berth=berthPool.getBerth();
         }catch (ResourceException e){
             logger.error(e.getMessage());
         }
         return berth;
     }

     public void returnBerth(Berth berth){
         berthPool.returnBerth(berth);
     }

     public boolean setContainer(Container container){
         lock.lock();
         boolean added;
         try{
             added = containers.offer(container);
         }finally {
             lock.unlock();
         }
         return added;
     }

     public Container getContainer(){
         lock.lock();
         Container container;
         try{
             container = containers.poll();
         }finally {
             lock.unlock();
         }
         return container;
     }
 }