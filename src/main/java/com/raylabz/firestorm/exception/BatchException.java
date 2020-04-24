package com.raylabz.firestorm.exception;

/**
 * An exception thrown during a transaction.
 */
public class BatchException extends FirestormException {

    public BatchException(final Exception e) {
        super(e);
    }

}
