package com.epam.multithreading.Exception;

public class ResourceException extends Exception {

    public ResourceException(String message, Throwable cause){
        super(message,cause);
    }

    public ResourceException(String message){
        super(message);
    }
}
