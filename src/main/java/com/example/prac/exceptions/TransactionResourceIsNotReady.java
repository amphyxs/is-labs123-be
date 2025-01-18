package com.example.prac.exceptions;

public class TransactionResourceIsNotReady extends RuntimeException {
    public TransactionResourceIsNotReady(String resource) {
        super("Resource " + resource + " is not ready for 2PC");
    }
}
