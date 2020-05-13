package com.raylabz.firestorm.exception;

public class NotInitializedException extends NullPointerException {

    public NotInitializedException() {
        super("Firestorm has not been initialized. Initialize Firebase using the Admin SDK and then call Firestorm.init() to initialize Firestorm.");
    }
}
