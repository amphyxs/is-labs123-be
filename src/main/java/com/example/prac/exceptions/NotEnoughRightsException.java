package com.example.prac.exceptions;

public class NotEnoughRightsException extends RuntimeException {
    public NotEnoughRightsException(String message) {
        super(message);
    }
}