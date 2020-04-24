package com.raylabz.firestorm.exception;

/**
 * Exception thrown by Firestorm.
 */
public class FirestormException extends RuntimeException {

    public FirestormException(final Exception e) {
        super(e.getMessage());
    }

    public FirestormException(String message) {
        super(message);
    }

}
