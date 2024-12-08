package com.example.prac.exceptions;

public class DragonWithSameNameException extends RuntimeException {
    public DragonWithSameNameException(String name) {
        super("Can't create dragon with same name: " + name);
    }
}
