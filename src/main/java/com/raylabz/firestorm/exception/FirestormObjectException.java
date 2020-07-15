package com.raylabz.firestorm.exception;

/**
 * An exception thrown when a class is checked but does not conform to Firestorm guidelines for class definitions:
 * a) The class is not annotated with @FirestormObject.
 * b) The class does not contain an ID field with type String.
 * c) The class does not have a public getter for the ID field.
 * d) The class does not have an empty public constructor.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class FirestormObjectException extends Exception {

    /**
     * Constructs a new FirestormObjectException.
     * @param message The exception's message.
     */
    public FirestormObjectException(String message) {
        super(message);
    }

}
