package com.raylabz.firestorm.rdb.callables;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.rdb.RDB;

import java.util.concurrent.Callable;

/**
 * Manages item existence operations.
 */
public class ItemExistsCallable implements Callable<Boolean> {

    private final DatabaseReference reference;

    private Boolean exists = null;

    /**
     * Constructs the callable.
     * @param reference The reference used in the callable.
     */
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
                Thread.sleep(RDB.CALLABLE_UPDATE_DELAY);
            }
            reference.removeEventListener(valueEventListener);
        } catch (InterruptedException e) {
            throw new FirestormException(e);
        }
        return exists;
    }

    /**
     * Checks if the reference exists or not.
     * @return Returns true if the reference exists, false otherwise.
     */
    public Boolean exists() {
        return exists;
    }

}
