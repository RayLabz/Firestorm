package com.raylabz.firestorm;

/**
 * Abstracts a Firestorm operation, its execution and callbacks.
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

    /**
     * Operation success callback.
     */
    public abstract void onSuccess();

    /**
     * Operation failure callback
     * @param e Exception (if any) thrown by the operation.
     */
    public abstract void onFailure(final Exception e);

}
