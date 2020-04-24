package com.raylabz.firestorm.exception;

/**
 * An exception thrown during a transaction.
 */
public class TransactionException extends FirestormException {

    public TransactionException(final Exception e) {
        super(e);
    }

}
