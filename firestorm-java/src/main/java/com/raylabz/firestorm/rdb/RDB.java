package com.raylabz.firestorm.rdb;

import com.google.api.core.*;
import com.google.cloud.firestore.*;
import com.google.common.util.concurrent.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.*;
import com.google.firebase.database.Transaction;
import com.raylabz.firestorm.*;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;
import com.raylabz.firestorm.exception.*;
import com.raylabz.firestorm.rdb.callables.GetMultipleItemsCallable;
import com.raylabz.firestorm.rdb.callables.GetSingleItemCallable;
import com.raylabz.firestorm.util.Reflector;

import javax.annotation.Nonnull;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * RDB (Firestore) provides an API that enables operations to be carried out for the Real Time Database.
 */
public final class RDB {

    /**
     * Static Firestore object used to make queries on Firestore.
     */
    static FirebaseDatabase rdb;

    /**
     * Stores a list of listeners registered to objects.
     */
//    private static final HashMap<Object, ListenerRegistration> registeredObjectListeners = new HashMap<>();
//    private static final HashMap<Class<?>, ListenerRegistration> registeredClassListeners = new HashMap<>();

    /**
     * Initializes the Real-Time Database API for Firestorm <b><u>after Firebase has been initialized</u></b> using <i>Firebase.initializeApp()</i>.
     */
    public static void init(String url) {
        rdb = FirebaseDatabase.getInstance(url);
    }


    /**
     * Enables offline mode for RDB. This allows data to be stored locally on the disk.
     */
    public static void enableOffline() {
        rdb.setPersistenceEnabled(true);
    }

    /**
     * Disables offline mode for RDB.
     */
    public static void disableOffline() {
        rdb.setPersistenceEnabled(false);
    }

    /**
     * Private constructor.
     */
    private RDB() { }

    /**
     * Retrieves the real-time database object being managed by Firestorm.
     * @return Returns a {@link FirebaseDatabase}.
     */
    public static FirebaseDatabase getRDB() {
        return rdb;
    }

    /**
     * Sets the value of an object in the real-time database.
     * @param object The object to set.
     * @return Returns Void.
     * @throws FirestormException thrown when the object has no ID, or when the object cannot be written.
     */
    public static FSFuture<Void> set(final Object object) throws FirestormException {
        try {
            Firestorm.checkRegistration(object);
            String idFieldValue = Reflector.getIDFieldValue(object);
            if (idFieldValue == null) {
                throw new FirestormException("ID field cannot be null");
            }
            DatabaseReference reference = rdb.getReference(object.getClass().getSimpleName() + "/" + idFieldValue);
            ApiFuture<Void> apiFuture = reference.setValueAsync(object);
            return FSFuture.fromAPIFuture(apiFuture);
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Sets the value of multiple objects.
     * @param objects A list of objects.
     * @return Returns an {@link FSFuture}
     * @param <T> The type of object.
     */
    public static <T> FSFuture<Void> set(List<T> objects) {
        try {
            if (!objects.isEmpty()) {
                Firestorm.checkRegistration(objects.get(0).getClass());
                DatabaseReference classReference = rdb.getReference(objects.get(0).getClass().getSimpleName());
                Map<String, Object> data = new HashMap<>();
                for (T object : objects) {
                    String id = Reflector.getIDFieldValue(object);
                    if (id == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    data.put(id, object);
                }
                ApiFuture<Void> future = classReference.updateChildrenAsync(data);
                return FSFuture.fromAPIFuture(future);
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Sets the value of multiple objects.
     * @param objects A varargs array of objects.
     * @return Returns an {@link FSFuture}.
     * @param <T> The type of object.
     */
    @SafeVarargs
    public static <T> FSFuture<Void> set(T... objects) {
        try {
            if (objects.length != 0) {
                Firestorm.checkRegistration(objects[0].getClass());
                DatabaseReference classReference = rdb.getReference(objects[0].getClass().getSimpleName());
                Map<String, Object> data = new HashMap<>();
                for (T object : objects) {
                    String id = Reflector.getIDFieldValue(object);
                    if (id == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    data.put(id, object);
                }
                ApiFuture<Void> future = classReference.updateChildrenAsync(data);
                return FSFuture.fromAPIFuture(future);
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Retrieves an object from RDB.
     * @param objectClass The object's class.
     * @param objectID The object's ID.
     * @return Returns an {@link FSFuture}.
     * @param <T> The type of object to return.
     */
    public static <T> FSFuture<T> get(final Class<T> objectClass, final String objectID) {
        DatabaseReference reference = rdb.getReference(objectClass.getSimpleName() + "/" + objectID);
        GetSingleItemCallable<T> getSingleItemCallable = new GetSingleItemCallable<>(objectClass, reference);
        return FSFuture.fromCallable(getSingleItemCallable);
    }

    /**
     * Retrieves multiple objects from the database using a list of IDs.
     * @param aClass The object type.
     * @param ids A list of IDs.
     * @return Returns an {@link FSFuture}.
     * @param <T> The type of objects.
     */
    public static <T> FSFuture<List<T>> get(final Class<T> aClass, List<String> ids) {
        List<DatabaseReference> databaseReferences = new ArrayList<>();
        for (String id : ids) {
            DatabaseReference databaseReference = rdb.getReference(aClass.getSimpleName()).child(id);
            databaseReferences.add(databaseReference);
        }
        GetMultipleItemsCallable<T> getMultipleItemsCallable = new GetMultipleItemsCallable<>(aClass, databaseReferences);
        return FSFuture.fromCallable(getMultipleItemsCallable);
    }

    /**
     * Retrieves multiple objects from the database using a varargs array of IDs.
     * @param aClass The object type.
     * @param ids A varargs array of IDs.
     * @return Returns an {@link FSFuture}.
     * @param <T> The type of objects.
     */
    public static <T> FSFuture<List<T>> get(final Class<T> aClass, String... ids) {
        List<DatabaseReference> databaseReferences = new ArrayList<>();
        for (String id : ids) {
            DatabaseReference databaseReference = rdb.getReference(aClass.getSimpleName()).child(id);
            databaseReferences.add(databaseReference);
        }
        GetMultipleItemsCallable<T> getMultipleItemsCallable = new GetMultipleItemsCallable<>(aClass, databaseReferences);
        return FSFuture.fromCallable(getMultipleItemsCallable);
    }

}
