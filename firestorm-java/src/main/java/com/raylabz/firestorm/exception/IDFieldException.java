package com.raylabz.firestorm.exception;

/**
 * An exception thrown when a class contains multiple fields marked as IDs.
 * @author Nicos Kasenides
 * @version 2.0.0
 */
public class IDFieldException extends Exception {

    /**
     * Constructs a new MultipleIDFieldsException.
     * @param message The exception's message.
     */
    public IDFieldException(String message) {
        super(message);
    }

}
