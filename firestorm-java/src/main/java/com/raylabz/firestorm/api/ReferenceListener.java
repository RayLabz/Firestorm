package com.raylabz.firestorm.api;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;

public abstract class ReferenceListener<T> implements EventListener<DocumentSnapshot> {

    /**
     * The listener is listening for changes to this object.
     */
    protected final Class<T> objectClass;

    /**
     * The document ID of the object to listen to.
     */
    protected final String documentID;

    /**
     * A callback to execute when an update is received.
     */
    protected final RealtimeUpdateCallback<T> callback;

    /**
     * Instantiates an OnReferenceUpdateListener.
     * @param objectClass The type of object this listener will be attached to.
     * @param documentID The document ID of the object.
     */
    protected ReferenceListener(Class<T> objectClass, String documentID, RealtimeUpdateCallback<T> callback) {
        this.objectClass = objectClass;
        this.documentID = documentID;
        this.callback = callback;
    }

    /**
     * Retrieves the object being listened at by this listener.
     * @return Returns the object being listened at by this listener.
     */
    public Class<T> getObjectClass() {
        return objectClass;
    }

    /**
     * Retrieves the ID of the document being listened to by this listener.
     * @return Returns the ID of the document listened to.
     */
    public String getDocumentID() {
        return documentID;
    }

}
