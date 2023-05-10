package com.raylabz.firestorm.async;

/**
 * An object-oriented real-time update callback interface.
 * @param <T> The type of object.
 */
public interface RealtimeUpdateCallback<T> {

    /**
     * Performs an operations when data is updated.
     * @param data The updated data.
     */
    void onUpdate(T data);

    /**
     * Executes code to handle an error.
     * @param t The error.
     */
    void onError(Throwable t);

}
