package com.raylabz.firestorm.exception;

/**
 * An exception thrown during a transaction.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class TransactionException extends FirestormException {

    /**
     * Constructs a TransactionException.
     * @param e The actual occurring exception.
     */
    public TransactionException(final Exception e) {
        super(e);
    }

}
