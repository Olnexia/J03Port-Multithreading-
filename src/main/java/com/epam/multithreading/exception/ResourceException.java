package com.epam.multithreading.exception;

public class ResourceException extends Exception {
    public ResourceException(String message, Throwable cause){
        super(message,cause);
    }

    public ResourceException(String message){
        super(message);
    }
}
