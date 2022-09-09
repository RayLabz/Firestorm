package com.raylabz.firestorm.exception;

/**
 * An exception thrown by setting too many batch operations (over 500).
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class TooManyOperationsException extends RuntimeException {

    /**
     * Constructs a TooManyOperationsException.
     * @param message The exception's message.
     */
    public TooManyOperationsException(final String message) {
        super(message);
    }

}
