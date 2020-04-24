package com.raylabz.firestorm;

import com.raylabz.firestorm.exception.FirestormException;

/**
 * Abstracts a Firestorm operation, its execution and callbacks.
 */
public abstract class FirestormOperation implements OnCompletionListener {

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
