package com.raylabz.firestorm;

public interface OnFailureListener {

    /**
     * Operation failure callback
     * @param e Exception (if any) thrown by the operation.
     */
    void onFailure(final Exception e);

}
