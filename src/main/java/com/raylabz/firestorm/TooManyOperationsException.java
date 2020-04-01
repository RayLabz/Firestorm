package com.raylabz.firestorm;

/**
 * An exception thrown by setting too many batch operations (over 500).
 */
public class TooManyOperationsException extends RuntimeException {

    public TooManyOperationsException(final String message) {
        super(message);
    }

}
