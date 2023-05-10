package com.raylabz.firestorm.rdb.callables;

import com.google.firebase.database.*;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.rdb.RDB;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Manages filtering operations.
 * @param <T> The type of object used.
 */
public class FilterableCallable<T> implements Callable<List<T>> {

    private final Query query;
    private final Class<T> objectClass;
    private List<T> data = null;
    private long numOfChildren = 0;

    private Throwable error = null;

    /**
     * Constructs the callable.
     * @param objectClass The object class.
     * @param query The query used to construct the callable.
     */
    public FilterableCallable(Class<T> objectClass, Query query) {
        this.query = query;
        this.objectClass = objectClass;
    }

    @Override
    public List<T> call() {
        try {

            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            if (data == null) {
                                numOfChildren = dataSnapshot.getChildrenCount();
                                data = new ArrayList<>();
                            }
                            for (DataSnapshot child : children) {
                                data.add(child.getValue(objectClass));
                            }
                        }
                    }
                    error = new FirestormException("Item does not exist.");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new FirestormException(databaseError.getMessage());
                }
            };

            query.addListenerForSingleValueEvent(listener);

            while ((data == null || data.size() < numOfChildren) && error == null) {
                Thread.sleep(RDB.CALLABLE_UPDATE_DELAY);
            }

            //Remove all the listeners:
            query.removeEventListener(listener);

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
