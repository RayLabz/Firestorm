package com.raylabz.firestorm;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Implements logic for Firestore update events.
 * @param <T> The class of objects this listener can be attached to.
 */
public abstract class FirestormEventListener<T> implements EventListener<DocumentSnapshot> {

    private static final String NO_SNAPSHOT_EXISTS_MESSAGE = "This object does not exist [No snapshot].";

    /**
     * The class of the object listened to by the event listener.
     */
    private final Class<? extends FirestormObject> objectClass;

    /**
     * The listener is listening for changes to this object.
     */
    private FirestormObject objectToListenFor;

    /**
     * Instantiates a FirestormEventListener.
     */
    public FirestormEventListener(FirestormObject object) {
        this.objectClass = object.getClass();
        this.objectToListenFor = object;
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
            FirestormObject fetchedObject = documentSnapshot.toObject(objectClass);
            if (objectToListenFor != null) {
                final ArrayList<ListenerRegistration> oldListeners = objectToListenFor.getListeners();
                objectToListenFor = fetchedObject;
                objectToListenFor.setListeners(oldListeners);
                onSuccess();
            }
            else {
                onFailure("Failed to retrieve update to object.");
            }
        }
        else {
            onFailure(NO_SNAPSHOT_EXISTS_MESSAGE);
        }
    }

    /**
     * Implements logic upon success of data update delivery.
     */
    public abstract void onSuccess();

    /**
     * Implements logic upon failure of data update delivery.
     * @param failureMessage The message of the failure.
     */
    public abstract void onFailure(final String failureMessage);

}
