package com.raylabz.firestorm.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.api.FirestormOperation;
import com.raylabz.firestorm.api.BasicWriteOperationProvider;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.exception.*;
import com.raylabz.firestorm.util.Reflector;

import java.util.List;

/**
 * Enables Firestore batch writes.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public abstract class FSBatch extends FirestormOperation implements BasicWriteOperationProvider<Void, BatchException> {

    public static final int MAX_OPERATIONS = 500;
    private int numOfOperations = 0;
    private WriteBatch batch;

    /**
     * Initializes the batch.
     */
    public FSBatch() {
        this.batch = FS.firestore.batch();
    }

    /**
     * Creates a Firestore document from an object as part of a batch write.
     * @param object The object containing the data.
     * @throws BatchException Thrown when the batch execution encounters an error.
     */
    public final Void create(final Object object) throws BatchException {
        try {
            Firestorm.checkRegistration(object);
            String idFieldValue = Reflector.getIDFieldValue(object);
            if (idFieldValue == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = FS.firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
            batch = batch.create(reference, object);
            numOfOperations++;
        } catch (IllegalAccessException | ClassRegistrationException | NoSuchFieldException | IDFieldException e) {
            throw new BatchException(e);
        }
        return null;
    }

    /**
     * Updates an object as part of a batch write.
     * @param object The object to update.
     * @throws BatchException Thrown when the batch execution encounters an error.
     */
    public final Void update(final Object object) throws BatchException {
        try {
            Firestorm.checkRegistration(object);
            String idFieldValue = Reflector.getIDFieldValue(object);
            if (idFieldValue == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = FS.firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
            batch = batch.set(reference, object);
            numOfOperations++;
        } catch (IllegalAccessException | ClassRegistrationException | NoSuchFieldException | IDFieldException e) {
            throw new BatchException(e);
        }
        return null;
    }

    /**
     * Deletes an object as part of a batch write.
     * @param object The object to delete.
     * @throws BatchException Thrown when the batch execution encounters an error.
     */
    public final Void delete(final Object object) throws BatchException {
        try {
            Firestorm.checkRegistration(object);
            final String documentID = Reflector.getIDFieldValue(object);
            if (documentID == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = FS.firestore.collection(object.getClass().getSimpleName()).document(documentID);
            batch = batch.delete(reference);
            Reflector.setIDFieldValue(object, null);
            numOfOperations++;
        } catch (IllegalAccessException | ClassRegistrationException | NoSuchFieldException | IDFieldException e) {
            throw new BatchException(e);
        }
        return null;
    }

    /**
     * Deletes an object using an ID.
     * @param objectClass The class of the object.
     * @param objectID The object's ID.
     * @throws BatchException Thrown when the batch execution encounters an error.
     */
    public final <T> Void delete(final Class<T> objectClass, final String objectID) throws BatchException {
        try {
            Firestorm.checkRegistration(objectClass);
            final DocumentReference reference = FS.firestore.collection(objectClass.getSimpleName()).document(objectID);
            batch = batch.delete(reference);
            numOfOperations++;
        } catch (ClassRegistrationException e) {
            throw new BatchException(e);
        }
        return null;
    }

    /**
     * Performs a batch operation.
     */
    FSFuture<List<WriteResult>> commit() {
        if (numOfOperations > MAX_OPERATIONS) {
            throw new TooManyOperationsException("The number of operations in a batch write cannot exceed 500.");
        }
        managedExecute();
        ApiFuture<List<WriteResult>> commitResult = batch.commit();
        return FSFuture.fromAPIFuture(commitResult);
    }

}
