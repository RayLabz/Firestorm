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
 * Manages the retrieval of multiple items.
 * @param <T> The object class.
 */
public class GetMultipleItemsCallable<T> implements Callable<List<T>> {

    private final List<DatabaseReference> references;
    private final Class<T> objectClass;
    private final List<T> data = new ArrayList<>();

    private Throwable error = null;

    /**
     * Constructs a callable.
     * @param objectClass The object class.
     * @param references The reference list.
     */
    public GetMultipleItemsCallable(Class<T> objectClass, List<DatabaseReference> references) {
        this.references = references;
        this.objectClass = objectClass;
    }

    @Override
    public List<T> call() {
        try {

            Map<DatabaseReference, ValueEventListener> referenceListeners = new HashMap<>();

            for (DatabaseReference reference : references) {
                ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            T object = dataSnapshot.getValue(objectClass);
                            data.add(object);
                        }
                        else {
                            error = new FirestormException("Reference '" + reference + "' does not exist.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw new FirestormException(databaseError.getMessage());
                    }
                });
                referenceListeners.put(reference, valueEventListener);
            }

            while (data.size() < references.size() && error == null) {
                Thread.sleep(RDB.CALLABLE_UPDATE_DELAY);
            }

            //Remove all the listeners:
            for (DatabaseReference reference : references) {
                ValueEventListener valueEventListener = referenceListeners.get(reference);
                reference.removeEventListener(valueEventListener);
            }

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
