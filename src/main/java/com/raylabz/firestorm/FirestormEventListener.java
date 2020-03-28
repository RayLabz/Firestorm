package com.raylabz.firestorm;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;

import javax.annotation.Nullable;

/**
 * Implements logic for Firestore update events.
 * @param <T> The class of objects this listener can be attached to.
 */
public abstract class FirestormEventListener<T> implements EventListener<DocumentSnapshot> {

    private static final String NO_SNAPSHOT_EXISTS_MESSAGE = "This object does not exist [No snapshot].";

    /**
     * The class of the object listened to by the event listener.
     */
    private final Class<T> objectClass;

    /**
     * Instantiates a FirestormEventListener.
     * @param objectClass The class of objects this listener can be attached to.
     */
    public FirestormEventListener(Class<T> objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * Executes when an update to an object is made.
     * @param documentSnapshot The document snapshot retrieved upon update.
     * @param e An exception thrown by Firestore if the data retrieval was not successful.
     */
    @Override
    public final void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirestoreException e) {
        if (e != null) {
            onFailure(e.getMessage());
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            T object = documentSnapshot.toObject(objectClass);
            onSuccess(object);
        }
        else {
            onFailure(NO_SNAPSHOT_EXISTS_MESSAGE);
        }
    }

    /**
     * Implements logic upon success of data update delivery.
     * @param object The object being updated.
     */
    public abstract void onSuccess(T object);

    /**
     * Implements logic upon failure of data update delivery.
     * @param failureMessage The message of the failure.
     */
    public abstract void onFailure(final String failureMessage);

    /**
     * Retrieves the class attribute of this FirestormEventListener.
     * @return Returns a class.
     */
    public Class<T> getObjectClass() {
        return objectClass;
    }

}
