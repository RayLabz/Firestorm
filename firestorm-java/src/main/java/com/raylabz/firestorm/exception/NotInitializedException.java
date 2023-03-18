package com.raylabz.firestorm.exception;

/**
 * An exception thrown when the user attempts to use com.raylabz.firestorm.Firestorm without initializing either a Firebase app and/or com.raylabz.firestorm.Firestorm itself.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class NotInitializedException extends NullPointerException {

    /**
     * Constructs a NotInitializedException.
     */
    public NotInitializedException() {
        super("com.raylabz.firestorm.Firestorm has not been initialized. Initialize Firebase using the Admin SDK and then call com.raylabz.firestorm.Firestorm.init() to initialize com.raylabz.firestorm.Firestorm.");
    }

}
