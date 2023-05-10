package com.raylabz.firestorm.api;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;

/**
 * Implements functionality for listening to objects.
 * @param <T> The type of object.
 */
public abstract class ObjectListener<T> implements EventListener<DocumentSnapshot> {

    //A message shown when the object does not exist.
    protected static final String NO_SNAPSHOT_EXISTS_MESSAGE = "This object does not exist [No snapshot].";

    /**
     * The listener is listening for changes to this object.
     */
    protected final T objectToListenFor;

    /**
     * A callback to execute when an update is received.
     */
    protected final RealtimeUpdateCallback<T> callback;

    /**
     * Instantiates a FirestormEventListener.
     * @param objectToListenFor The object to listen for updates.
     * @param callback The callback function to execute code when an update is received.
     */
    protected ObjectListener(T objectToListenFor, RealtimeUpdateCallback<T> callback) {
        this.objectToListenFor = objectToListenFor;
        this.callback = callback;
    }

    /**
     * Returns the object being listened at by this listener.
     * @return Returns the object being listened at by this listener.
     */
    public T getObjectToListenFor() {
        return objectToListenFor;
    }

}
