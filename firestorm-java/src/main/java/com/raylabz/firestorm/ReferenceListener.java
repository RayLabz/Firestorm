package com.raylabz.firestorm;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;

import javax.annotation.Nullable;

/**
 * Implements logic for Firestore update events.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class ReferenceListener<T> implements EventListener<DocumentSnapshot> {

    /**
     * The listener is listening for changes to this object.
     */
    private final Class<T> objectClass;

    /**
     * The document ID of the object to listen to.
     */
    private final String documentID;

    /**
     * A callback to execute when an update is received.
     */
    private final RealtimeUpdateCallback<T> callback;

    /**
     * Instantiates an OnReferenceUpdateListener.
     * @param objectClass The type of object this listener will be attached to.
     * @param documentID The document ID of the object.
     */
    public ReferenceListener(Class<T> objectClass, String documentID, RealtimeUpdateCallback<T> callback) {
        this.objectClass = objectClass;
        this.documentID = documentID;
        this.callback = callback;
    }

    /**
     * Executes when an update to an object is made.
     * @param documentSnapshot The document snapshot retrieved upon update.
     * @param e An exception thrown by Firestore if the data retrieval was not successful.
     */
    @Override
    public final void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
        if (e != null) {
            callback.onError(e);
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {

            T fetchedObject = documentSnapshot.toObject(objectClass);

            if (fetchedObject != null) {

                if (fetchedObject.getClass() != objectClass) {
                    callback.onError(new RuntimeException("The type of the event listener's received object does not match the type provided."));
                    return;
                }

                callback.onUpdate(fetchedObject);

            }
            else {
                callback.onError(new RuntimeException("Failed to retrieve update to object."));
            }
        }
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
