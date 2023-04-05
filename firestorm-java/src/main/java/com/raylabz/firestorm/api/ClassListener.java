package com.raylabz.firestorm.api;

import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.QuerySnapshot;
import com.raylabz.firestorm.firestore.FSObjectChange;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;

import java.util.List;

/**
 * Abstract class listening.
 * @param <T> The type of the object listened to.
 * @author Nicos Kasenides
 * @version 2.0.0
 */
public abstract class ClassListener<T> implements EventListener<QuerySnapshot> {

    /**
     * The listener is listening for changes to this object.
     */
    protected final Class<T> objectClass;

    /**
     * A callback to execute when an update is received.
     */
    protected final RealtimeUpdateCallback<List<FSObjectChange<T>>> callback;

    /**
     * Instantiates an OnReferenceUpdateListener.
     * @param objectClass The type of object this listener will be attached to.
     */
    protected ClassListener(Class<T> objectClass, RealtimeUpdateCallback<List<FSObjectChange<T>>> callback) {
        this.objectClass = objectClass;
        this.callback = callback;
    }

    /**
     * Retrieves the object being listened at by this listener.
     * @return Returns the object being listened at by this listener.
     */
    public Class<?> getObjectClass() {
        return objectClass;
    }

}
