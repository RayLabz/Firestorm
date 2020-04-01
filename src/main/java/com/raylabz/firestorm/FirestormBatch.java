package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Enables Firestore batch writes.
 */
public abstract class FirestormBatch extends FirestormOperation {

    private int numOfOperations = 0;
    private WriteBatch batch;

    /**
     * Initializes the batch.
     */
    public FirestormBatch() {
        this.batch = Firestorm.firestore.batch();
    }

    /**
     * Creates a Firestore document from an object as part of a batch write.
     * @param object The object containing the data.
     */
    public final void create(final FirestormObject object) {
        final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document();
        object.setId(reference.getId());
        batch = batch.create(reference, object);
        numOfOperations++;
    }

    /**
     * Updates an object as part of a batch write.
     * @param object The object to update.
     */
    public final void update(final FirestormObject object) {
        final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document(object.getId());
        batch = batch.set(reference, object);
        numOfOperations++;
    }

    /**
     * Deletes an object as part of a batch write.
     * @param object The object to delete.
     */
    public final void delete(final FirestormObject object) {
        final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document(object.getId());
        batch = batch.delete(reference);
        object.setId(null);
        numOfOperations++;
    }

    /**
     * Performs a batch operation.
     */
    void doBatch() {
        if (numOfOperations > 500) {
            throw new TooManyOperationsException("The number of operations in a batch write cannot exceed 500.");
        }

        try {
            managedExecute();
            final ApiFuture<List<WriteResult>> commitResult = batch.commit();
            commitResult.get();
        } catch (InterruptedException | ExecutionException | FirestormException e) {
            onFailure(e);
            return;
        }

        onSuccess();
    }

}
