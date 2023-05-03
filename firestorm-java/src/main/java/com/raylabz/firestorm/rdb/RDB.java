package com.raylabz.firestorm.rdb;

import com.google.api.core.*;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.database.*;
import com.raylabz.firestorm.*;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.exception.*;
import com.raylabz.firestorm.firestore.FSFilterable;
import com.raylabz.firestorm.rdb.callables.*;
import com.raylabz.firestorm.util.Reflector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RDB (Firestore) provides an API that enables operations to be carried out for the Real Time Database.
 */
public final class RDB {

    /**
     * Static Firestore object used to make queries on Firerstore.
     */
    static FirebaseDatabase rdb;

    public static final int CALLABLE_UPDATE_DELAY = 2; //The delay between updates in callables.

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
    public static <T> FSFuture<T> get(final Class<T> objectClass, @Nonnull final String objectID) {
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
    public static <T> FSFuture<List<T>> get(final Class<T> aClass, @Nonnull String... ids) {
        List<DatabaseReference> databaseReferences = new ArrayList<>();
        for (String id : ids) {
            DatabaseReference databaseReference = rdb.getReference(aClass.getSimpleName()).child(id);
            databaseReferences.add(databaseReference);
        }
        GetMultipleItemsCallable<T> getMultipleItemsCallable = new GetMultipleItemsCallable<>(aClass, databaseReferences);
        return FSFuture.fromCallable(getMultipleItemsCallable);
    }

    /**
     * Check if an object exists in the database.
     * @param aClass The object class.
     * @param id The ID of the object.
     * @return Returns true if the object exists, false otherwise.
     * @param <T> The type of object.
     */
    public static <T> FSFuture<Boolean> exists(final Class<T> aClass, @Nonnull String id) {
        DatabaseReference reference = rdb.getReference(aClass.getSimpleName()).child(id);
        ItemExistsCallable<T> itemExistsCallable = new ItemExistsCallable<>(reference);
        return FSFuture.fromCallable(itemExistsCallable);
    }

    /**
     * Check if an object exists in the database.
     * @param object The object.
     * @return Returns true if the object exists, false otherwise.
     * @param <T> The type of object.
     */
    public static <T> FSFuture<Boolean> exists(final T object) {
        try {
            String id = Reflector.getIDFieldValue(object);
            if (id == null) {
                throw new FirestormException("ID field cannot be null");
            }
            DatabaseReference reference = rdb.getReference(object.getClass().getSimpleName()).child(id);
            ItemExistsCallable<T> itemExistsCallable = new ItemExistsCallable<>(reference);
            return FSFuture.fromCallable(itemExistsCallable);
        } catch (IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes an object from the database.
     * @param object The object to delete.
     * @return Returns an {@link FSFuture}.
     * @throws FirestormException thrown when the ID of the object is null, or the operation cannot be completed.
     */
    public static FSFuture<Void> delete(Object object) throws FirestormException {
        try {
            final String documentID = Reflector.getIDFieldValue(object);
            if (documentID == null) {
                throw new FirestormException("ID field cannot be null");
            }
            DatabaseReference reference = rdb.getReference(object.getClass().getSimpleName()).child(documentID);
            ApiFuture<Void> future = reference.removeValueAsync();
            FSFuture<Void> fsFuture = FSFuture.fromAPIFuture(future);
            Reflector.setIDFieldValue(object, null);
            return fsFuture;
        } catch (IllegalAccessException | NoSuchFieldException | NotInitializedException | IDFieldException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes an object from the database.
     * @param aClass The class of the object.
     * @param id The ID of the object.
     * @return Returns an {@link FSFuture}.
     * @throws FirestormException thrown when the ID of the object is null, or the operation cannot be completed.
     */
    public static FSFuture<Void> delete(Class<?> aClass, @Nonnull String id) throws FirestormException {
        try {
            DatabaseReference reference = rdb.getReference(aClass.getSimpleName()).child(id);
            ApiFuture<Void> future = reference.removeValueAsync();
            return FSFuture.fromAPIFuture(future);
        } catch (NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes objects from the database.
     * @param objects The objects to delete.
     * @return Returns an {@link FSFuture}.
     * @param <T> The type of the objects.
     */
    public static <T> FSFuture<Void> delete(List<T> objects) {
        if (!objects.isEmpty()) {
            try {
                Firestorm.checkRegistration(objects.get(0).getClass());
                ArrayList<DatabaseReference> databaseReferences = new ArrayList<>();
                for (T object : objects) {
                    String idFieldValue = Reflector.getIDFieldValue(object);
                    if (idFieldValue == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    DatabaseReference reference = rdb.getReference(objects.get(0).getClass().getSimpleName()).child(idFieldValue);
                    databaseReferences.add(reference);
                }
                DeleteMultipleItemsCallable<T> deleteMultipleItemsCallable = new DeleteMultipleItemsCallable<>(databaseReferences);
                return FSFuture.fromCallable(deleteMultipleItemsCallable);
            } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
                throw new FirestormException(e);
            }
        }
        throw new FirestormException("Objects array cannot be empty.");
    }

    /**
     * Deletes objects from the database.
     * @param objects The objects to delete.
     * @return Returns an {@link FSFuture}.
     * @param <T> The type of the objects.
     */
    @SafeVarargs
    public static <T> FSFuture<Void> delete(T... objects) {
        if (objects.length != 0) {
            try {
                Firestorm.checkRegistration(objects[0].getClass());
                ArrayList<DatabaseReference> databaseReferences = new ArrayList<>();
                for (T object : objects) {
                    String idFieldValue = Reflector.getIDFieldValue(object);
                    if (idFieldValue == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    DatabaseReference reference = rdb.getReference(objects[0].getClass().getSimpleName()).child(idFieldValue);
                    databaseReferences.add(reference);
                }
                DeleteMultipleItemsCallable<T> deleteMultipleItemsCallable = new DeleteMultipleItemsCallable<T>(databaseReferences);
                return FSFuture.fromCallable(deleteMultipleItemsCallable);
            } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
                throw new FirestormException(e);
            }
        }
        throw new FirestormException("Objects array cannot be empty.");
    }

    /**
     * Deletes objects of a particular class from the database based on an array of IDs.
     * @param aClass The class of objects to delete.
     * @param ids The IDs array.
     * @return Returns an {@link  FSFuture}.
     * @param <T> The object type.
     */
    public static <T> FSFuture<Void> delete(final Class<T> aClass, String... ids) {
        if (ids.length != 0) {
            try {
                Firestorm.checkRegistration(aClass);
                ArrayList<DatabaseReference> databaseReferences = new ArrayList<>();
                for (String id : ids) {
                    DatabaseReference reference = rdb.getReference(aClass.getSimpleName()).child(id);
                    databaseReferences.add(reference);
                }
                DeleteMultipleItemsCallable<T> deleteMultipleItemsCallable = new DeleteMultipleItemsCallable<T>(databaseReferences);
                return FSFuture.fromCallable(deleteMultipleItemsCallable);
            } catch (ClassRegistrationException e) {
                throw new FirestormException(e);
            }
        }
        throw new FirestormException("Objects array cannot be empty.");
    }

    /**
     * Deletes ALL objects of a certain type (WARNING: Destructive action).
     *
     * @param aClass The class/type.
     * @return Returns an {@link FSFuture}.
     * @param <T> The type.
     */
    public static <T> FSFuture<Void> deleteAllOfType(final Class<T> aClass) {
        try {
            Firestorm.checkRegistration(aClass);
            DatabaseReference reference = rdb.getReference(aClass.getSimpleName());
            ApiFuture<Void> future = reference.removeValueAsync();
            return FSFuture.fromAPIFuture(future);
        } catch (ClassRegistrationException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Lists all objects of a specified type up to a certain limit.
     *
     * @param aClass The class of the object.
     * @param limit The limit (return up to a certain number of items).
     * @return Returns list of {@link T}.
     * @param <T> The type of the object.
     */
    public static <T> FSFuture<List<T>> list(final Class<T> aClass, int limit) {
        DatabaseReference classReference = rdb.getReference(aClass.getSimpleName());
        ListItemsCallable<T> listItemsCallable = new ListItemsCallable<>(aClass, classReference, limit);
        return FSFuture.fromCallable(listItemsCallable);
    }

    /**
     * Lists all objects/documents of a specified type.
     *
     * @param aClass The class of the object.
     * @return Returns list of {@link T}.
     * @param <T> The type of the object.
     */
    public static <T> FSFuture<List<T>> list(final Class<T> aClass) {
        DatabaseReference classReference = rdb.getReference(aClass.getSimpleName());
        ListItemsCallable<T> listItemsCallable = new ListItemsCallable<>(aClass, classReference);
        return FSFuture.fromCallable(listItemsCallable);
    }

    /**
     * Lists a set documents which match the filtering criteria provided. Returns a filter of all documents if no filters are used.
     * @param objectClass The type of objects to filter.
     * @return Returns an {@link RDBFilterable}.
     * @param <T> The type of objects to filter.
     */
    public static <T> RDBFilterable<T> filter(final Class<T> objectClass) {
        return new RDBFilterable<>(rdb.getReference(objectClass.getSimpleName()), objectClass);
    }

}
