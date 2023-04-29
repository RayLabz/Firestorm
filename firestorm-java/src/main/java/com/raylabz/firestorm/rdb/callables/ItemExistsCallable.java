package com.raylabz.firestorm.rdb.callables;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.raylabz.firestorm.exception.FirestormException;

import java.util.concurrent.Callable;

public class ItemExistsCallable<T> implements Callable<Boolean> {

    private final DatabaseReference reference;

    private Boolean exists = null;

    public ItemExistsCallable(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    public Boolean call() {
        try {
            ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    exists = dataSnapshot.exists();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new FirestormException(databaseError.getMessage());
                }
            });
            while (exists == null) {
                Thread.sleep(25);
            }
            reference.removeEventListener(valueEventListener);
        } catch (InterruptedException e) {
            throw new FirestormException(e);
        }
        return exists;
    }

    public Boolean exists() {
        return exists;
    }

}
