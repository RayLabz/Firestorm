package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.raylabz.firestorm.exception.BatchException;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.exception.FirestormObjectException;
import com.raylabz.firestorm.exception.TooManyOperationsException;

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
    public final void create(final Object object) {
        try {
            Reflector.checkObject(object);
            final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document();
            Reflector.setIDField(object, reference.getId());
            batch = batch.create(reference, object);
            numOfOperations++;
        } catch (IllegalAccessException | FirestormObjectException | NoSuchFieldException e) {
            throw new BatchException(e);
        }
    }

    /**
     * Updates an object as part of a batch write.
     * @param object The object to update.
     */
    public final void update(final Object object) {
        try {
            Reflector.checkObject(object);
            final String id = Reflector.getIDField(object);
            final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document(id);
            batch = batch.set(reference, object);
            numOfOperations++;
        } catch (IllegalAccessException | FirestormObjectException | NoSuchFieldException e) {
            throw new BatchException(e);
        }
    }

    /**
     * Deletes an object as part of a batch write.
     * @param object The object to delete.
     */
    public final void delete(final Object object) {
        try {
            Reflector.checkObject(object);
            final String id = Reflector.getIDField(object);
            final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document(id);
            batch = batch.delete(reference);
            Reflector.setIDField(object, null);
            numOfOperations++;
        } catch (IllegalAccessException | FirestormObjectException | NoSuchFieldException e) {
            throw new BatchException(e);
        }
    }

    public final void delete(final Class<?> objectClass, final String objectID) {
        try {
            Reflector.checkClass(objectClass);
            final DocumentReference reference = Firestorm.firestore.collection(objectClass.getSimpleName()).document(objectID);
            batch = batch.delete(reference);
            numOfOperations++;
        } catch (FirestormObjectException e) {
            throw new BatchException(e);
        }
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
