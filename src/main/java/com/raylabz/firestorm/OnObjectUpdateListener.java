package com.raylabz.firestorm;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.firestore.ListenerRegistration;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.exception.FirestormObjectException;
import com.sun.corba.se.impl.io.TypeMismatchException;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Implements logic for Firestore update events.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public abstract class OnObjectUpdateListener implements EventListener<DocumentSnapshot> {

    private static final String NO_SNAPSHOT_EXISTS_MESSAGE = "This object does not exist [No snapshot].";

    /**
     * The listener is listening for changes to this object.
     */
    private final Object objectToListenFor;

    /**
     * Instantiates a FirestormEventListener.
     * @param object The object to attach the listener to.
     */
    public OnObjectUpdateListener(final Object object) {
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

        //Check ID of objectToListenFor:
        try {
            Reflector.checkObject(objectToListenFor);
            Field idField = Reflector.findUnderlyingIDField(objectToListenFor.getClass());
            if (idField == null) {
                onFailure("Missing ID field in class hierarchy for class '" + objectToListenFor.getClass().getSimpleName() + "'.");
                return;
            }
            boolean accessible = idField.isAccessible();
            idField.setAccessible(true);
            final String idValue = (String) idField.get(objectToListenFor);
            idField.setAccessible(accessible);
            if (idValue == null) {
                return;
            }
        } catch (IllegalAccessException | FirestormObjectException ex) {
            onFailure(ex.getMessage());
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            Object fetchedObject = documentSnapshot.toObject(objectToListenFor.getClass());

            if (fetchedObject != null) {
                if (fetchedObject.getClass() != objectToListenFor.getClass()) {
                    onFailure("The type of the event listener's received object does not match the type provided.");
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
                            onFailure(ex.getMessage());
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
                            onFailure(ex.getMessage());
                            return;
                        }
                    }
                }

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
     * Returns the object being listened at by this listener.
     * @return Returns the object being listened at by this listener.
     */
    public Object getObjectToListenFor() {
        return objectToListenFor;
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
