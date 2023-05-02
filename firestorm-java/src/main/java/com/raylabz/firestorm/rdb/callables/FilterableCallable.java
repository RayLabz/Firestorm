package com.raylabz.firestorm.rdb.callables;

import com.google.firebase.database.*;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.rdb.RDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class FilterableCallable<T> implements Callable<List<T>> {

    private final Query query;
    private final Class<T> objectClass;
    private List<T> data = null;

    private Throwable error = null;

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
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                data.add(child.getValue(objectClass));
                            }
                        }
                    }
                    throw new FirestormException("Could not retrieve filterable at location '" + query.getRef() + "'.");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new FirestormException(databaseError.getMessage());
                }
            };

            query.addListenerForSingleValueEvent(listener);

            while (data == null && error == null) {
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

    public List<T> getData() {
        return data;
    }

}
