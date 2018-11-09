 package com.epam.multithreading.entity;

 import java.util.concurrent.atomic.AtomicBoolean;
 import java.util.concurrent.locks.Lock;
 import java.util.concurrent.locks.ReentrantLock;

 public class Port {
     private static Port instance = null;
     private static AtomicBoolean inizialized;

     private static Lock lock = new ReentrantLock();

     private Port() {
     }

     public static Port getInstance() {

         if(!inizialized.get()) {
             try {
                 lock.lock();
                 if (!inizialized.get()) {
                     instance = new Port();
                     inizialized.set(true);
                 }
             } finally {
                 lock.unlock();
             }
         }
         return instance;
     }
 }
