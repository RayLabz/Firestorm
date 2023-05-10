package com.raylabz.firestorm.rdb.callables;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.rdb.RDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * A callable that manages item listing.
 * @param <T> The object type.
 */
public class ListItemsCallable<T> implements Callable<List<T>> {

    private final DatabaseReference classReference;
    private final Class<T> objectClass;
    private List<T> data = null;
    private final int limit;
    private boolean doneReading = false;

    private Throwable error = null;

    /**
     * Constructs a callable.
     * @param objectClass The object class.
     * @param classReference The reference.
     * @param limit The number of objects to retrieve.
     */
    public ListItemsCallable(Class<T> objectClass, DatabaseReference classReference, int limit) {
        this.classReference = classReference;
        this.objectClass = objectClass;
        this.limit = limit;
    }

    /**
     * Constructs a callable.
     * @param objectClass The object class.
     * @param classReference The reference
     */
    public ListItemsCallable(Class<T> objectClass, DatabaseReference classReference) {
        this.classReference = classReference;
        this.objectClass = objectClass;
        this.limit = 10;
    }

    @Override
    public List<T> call() {
        try {

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    data = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        T object = child.getValue(objectClass);
                        data.add(object);
                    }
                    doneReading = true;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    error = new FirestormException("Reference '" + classReference + "' does not exist.");
                }
            };

            classReference.orderByKey().limitToFirst(limit).addListenerForSingleValueEvent(valueEventListener);

            while (!doneReading && error == null) {
                Thread.sleep(RDB.CALLABLE_UPDATE_DELAY);
            }

            classReference.removeEventListener(valueEventListener);

            if (error != null) {
                System.err.println(error.getMessage());
            }
        } catch (Throwable e) {
            throw new FirestormException(e);
        }
        return data;
    }

    /**
     * Retrieves the data.
     * @return Returns a list.
     */
    public List<T> getData() {
        return data;
    }

}
