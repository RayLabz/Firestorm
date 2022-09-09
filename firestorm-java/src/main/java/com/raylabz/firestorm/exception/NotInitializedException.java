package com.raylabz.firestorm.exception;

/**
 * An exception thrown when the user attempts to use Firestorm without initializing either a Firebase app and/or Firestorm itself.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class NotInitializedException extends NullPointerException {

    /**
     * Constructs a NotInitializedException.
     */
    public NotInitializedException() {
        super("Firestorm has not been initialized. Initialize Firebase using the Admin SDK and then call Firestorm.init() to initialize Firestorm.");
    }

}
