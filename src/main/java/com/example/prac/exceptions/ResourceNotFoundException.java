package com.example.prac.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Class<?> cls) {
        super(String.format("%s not found", cls.getSimpleName()));
    }
}