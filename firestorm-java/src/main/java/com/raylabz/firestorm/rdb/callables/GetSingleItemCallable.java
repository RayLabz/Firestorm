package com.raylabz.firestorm.rdb.callables;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.exception.FirestormException;

import java.util.concurrent.Callable;

public class GetSingleItemCallable<T> implements Callable<T> {

    private final DatabaseReference reference;
    private final Class<T> objectClass;
    private T data = null;

    private Throwable error = null;

    public GetSingleItemCallable(Class<T> objectClass, DatabaseReference reference) {
        this.reference = reference;
        this.objectClass = objectClass;
    }

    @Override
    public T call() {
        try {
            ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        data = dataSnapshot.getValue(objectClass);
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
            while (data == null && error == null) {
                Thread.sleep(25);
            }
            reference.removeEventListener(valueEventListener);

            if (error != null) {
                System.err.println(error.getMessage());
            }
        } catch (InterruptedException e) {
            throw new FirestormException(e);
        }
        return data;
    }

    public T getData() {
        return data;
    }

}
