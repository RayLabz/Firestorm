package com.raylabz.firestorm;

/**
 * Defines what will be executed when an operation fails.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public interface OnFailureListener {

    /**
     * Operation failure callback
     * @param e Exception (if any) thrown by the operation.
     */
    void onFailure(final Exception e);

}
