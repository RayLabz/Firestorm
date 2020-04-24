package com.raylabz.firestorm.exception;

/**
 * An exception thrown by setting too many batch operations (over 500).
 */
public class TooManyOperationsException extends RuntimeException {

    public TooManyOperationsException(final String message) {
        super(message);
    }

}
