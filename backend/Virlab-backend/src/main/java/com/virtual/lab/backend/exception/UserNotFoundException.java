package com.virtual.lab.backend.exception;

public class UserNotFoundException  extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }

}
