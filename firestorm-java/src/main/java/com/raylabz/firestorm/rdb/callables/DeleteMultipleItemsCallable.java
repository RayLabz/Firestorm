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
 * Manages deletion operations.
 * @param <T> The object type.
 */
public class DeleteMultipleItemsCallable<T> implements Callable<Void> {

    private final List<DatabaseReference> references;

    private int completedOperations = 0;

    /**
     * Constructs the callable.
     * @param references The items to delete.
     */
    public DeleteMultipleItemsCallable(List<DatabaseReference> references) {
        this.references = references;
    }

    @Override
    public Void call() {
        try {
            for (DatabaseReference reference : references) {
                reference.removeValue((error, ref) -> completedOperations++);
            }

            while (completedOperations < references.size()) {
                Thread.sleep(RDB.CALLABLE_UPDATE_DELAY);
            }
        } catch (Throwable e) {
            throw new FirestormException(e);
        }
        return null;
    }

}
