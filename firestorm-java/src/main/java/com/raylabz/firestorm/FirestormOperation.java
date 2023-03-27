package com.raylabz.firestorm;

import com.raylabz.firestorm.exception.FirestormException;

/**
 * Abstracts a com.raylabz.firestorm.Firestorm operation, its execution and callbacks.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public abstract class FirestormOperation {

    /**
     * Managed execution.
     */
    public final void managedExecute() throws FirestormException {
        execute();
    }

    /**
     * Executes an operation.
     */
    public abstract void execute();

}
