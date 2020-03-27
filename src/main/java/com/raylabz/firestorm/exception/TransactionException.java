package com.raylabz.firestorm.exception;

public class TransactionException extends RuntimeException {

    public TransactionException(final Exception e) {
        super(e.getMessage());
    }

}
