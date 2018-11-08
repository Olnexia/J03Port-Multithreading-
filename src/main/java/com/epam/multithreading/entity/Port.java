 package com.epam.multithreading.entity;

 public class Port {
     private static Port instance = null;

     private Port() {
     }

     public static Port getInstance() {
         if (instance == null) {
             synchronized (Port.class) {
                 if (instance == null) {
                     instance = new Port();
                 }
             }
         }
         return instance;
     }
 }
