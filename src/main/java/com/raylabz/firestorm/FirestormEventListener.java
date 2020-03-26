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

    private final Class<T> aClass;

    /**
     * Instantiates a FirestormEventListener.
     * @param aClass The class of objects this listener can be attached to.
     */
    public FirestormEventListener(Class<T> aClass) {
        this.aClass = aClass;
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
            T object = documentSnapshot.toObject(aClass);
            onSuccess(object);
        }
        else {
            onFailure(NO_SNAPSHOT_EXISTS_MESSAGE);
        }
    }

    /**
     * Implements logic upon success of data update delivery.
     * @param object
     */
    public abstract void onSuccess(T object);

    /**
     * Implements logic upon failure of data update delivery.
     * @param failureMessage
     */
    public abstract void onFailure(final String failureMessage);

    /**
     * Retrieves the class attribute of this FirestormEventListener.
     * @return
     */
    public Class<T> getaClass() {
        return aClass;
    }

}
