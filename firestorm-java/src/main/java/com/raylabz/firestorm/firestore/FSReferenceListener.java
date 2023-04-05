package com.raylabz.firestorm.firestore;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.raylabz.firestorm.api.ReferenceListener;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;

import javax.annotation.Nullable;

/**
 * Implements logic for Firestore update events.
 * @author Nicos Kasenides
 * @version 2.0.0
 */
public class FSReferenceListener<T> extends ReferenceListener<T> {

    public FSReferenceListener(Class<T> objectClass, String documentID, RealtimeUpdateCallback<T> callback) {
        super(objectClass, documentID, callback);
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

}
