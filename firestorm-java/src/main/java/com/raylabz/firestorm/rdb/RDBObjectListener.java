package com.raylabz.firestorm.rdb;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FirestoreException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.api.ObjectListener;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;
import com.raylabz.firestorm.exception.ClassRegistrationException;
import com.raylabz.firestorm.exception.IDFieldException;
import com.raylabz.firestorm.util.Reflector;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Implements logic for RDB update events.
 * @author Nicos Kasenides
 * @version 2.0.0
 */
public class RDBObjectListener<T> implements ValueEventListener {

    //A message shown when the object does not exist.
    protected static final String NO_SNAPSHOT_EXISTS_MESSAGE = "This object does not exist [No snapshot].";

    private final T objectToListenFor;
    private final RealtimeUpdateCallback<T> callback;

    /**
     * Handles object retrieval.
     * @param dataSnapshot The snapshot received.
     */
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //Check ID of objectToListenFor:
        try {
            Firestorm.checkRegistration(objectToListenFor);
            Field idField = Reflector.findIDField(objectToListenFor.getClass());
            if (idField == null) {
                callback.onError(new RuntimeException("Missing ID field in class hierarchy for class '" + objectToListenFor.getClass().getSimpleName() + "'."));
                return;
            }
            boolean accessible = idField.isAccessible();
            idField.setAccessible(true);
            final String idValue = (String) idField.get(objectToListenFor);
            idField.setAccessible(accessible);
            if (idValue == null) {
                return;
            }
        } catch (IllegalAccessException | ClassRegistrationException | IDFieldException ex) {
            callback.onError(ex);
            return;
        }

        if (dataSnapshot != null && dataSnapshot.exists()) {
            Object fetchedObject = dataSnapshot.getValue(objectToListenFor.getClass());

            if (fetchedObject != null) {
                if (fetchedObject.getClass() != objectToListenFor.getClass()) {
                    callback.onError(new RuntimeException("The type of the event listener's received object does not match the type provided."));
                    return;
                }

                //Retrieve values for fields for this class:
                final Field[] declaredFields = objectToListenFor.getClass().getDeclaredFields();
                for (Field f : declaredFields) {
                    if (!Modifier.isStatic(f.getModifiers())) {
                        try {
                            final boolean accessible = f.isAccessible();
                            f.setAccessible(true);
                            final Object valueOfFetchedObject = f.get(fetchedObject);
                            f.set(objectToListenFor, valueOfFetchedObject);
                            f.setAccessible(accessible);
                        } catch (IllegalAccessException ex) {
                            callback.onError(ex);
                            return;
                        }
                    }
                }

                //Retrieve values for non-static fields for this class' superclasses:
                final ArrayList<Field> superclassFields = Reflector.getSuperclassFields(objectToListenFor.getClass(), Object.class);
                for (Field f : superclassFields) {
                    if (!Modifier.isStatic(f.getModifiers())) {
                        try {
                            final boolean accessible = f.isAccessible();
                            f.setAccessible(true);
                            final Object valueOfFetchedObject = f.get(fetchedObject);
                            f.set(objectToListenFor, valueOfFetchedObject);
                            f.setAccessible(accessible);
                        } catch (IllegalAccessException ex) {
                            callback.onError(ex);
                            return;
                        }
                    }
                }

                callback.onUpdate(objectToListenFor);

            }
            else {
                callback.onError(new RuntimeException("Failed to retrieve update to object."));
            }
        }
        else {
            callback.onError(new RuntimeException(NO_SNAPSHOT_EXISTS_MESSAGE));
        }
    }

    /**
     * Executes code when the operation fails.
     * @param databaseError The error.
     */
    @Override
    public void onCancelled(DatabaseError databaseError) {
        callback.onError(new RuntimeException(databaseError.getMessage()));
    }

    /**
     * Constructs an object listener.
     * @param objectToListenFor The object being listened to.
     * @param callback Calls code when the object is changed.
     */
    public RDBObjectListener(T objectToListenFor, RealtimeUpdateCallback<T> callback) {
        this.objectToListenFor = objectToListenFor;
        this.callback = callback;
    }

}
