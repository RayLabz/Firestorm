package com.raylabz.firestorm.exception;

public class TooManyOperationsException extends RuntimeException {

    public TooManyOperationsException(final String message) {
        super(message);
    }

}
