package com.example.prac.exceptions;

public class PasswordHashingException extends RuntimeException {
    public PasswordHashingException() {
        super("Error while hashing password");
    }
}
