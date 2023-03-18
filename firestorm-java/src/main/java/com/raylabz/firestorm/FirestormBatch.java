//package com.raylabz.firestorm;
//
//import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.DocumentReference;
//import com.google.cloud.firestore.WriteBatch;
//import com.google.cloud.firestore.WriteResult;
//import com.raylabz.firestorm.exception.*;
//
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
///**
// * Enables Firestore batch writes.
// * @author Nicos Kasenides
// * @version 1.0.0
// */
//public abstract class FirestormBatch extends FirestormOperation {
//
//    private int numOfOperations = 0;
//    private WriteBatch batch;
//
//    /**
//     * Initializes the batch.
//     */
//    public FirestormBatch() {
//        this.batch = FS.firestore.batch();
//    }
//
//    /**
//     * Creates a Firestore document from an object as part of a batch write.
//     * @param object The object containing the data.
//     * @throws BatchException Thrown when the batch execution encounters an error.
//     */
//    public final void create(final Object object) throws BatchException {
//        try {
//            FS.checkRegistration(object);
//            final DocumentReference reference = FS.firestore.collection(object.getClass().getSimpleName()).document();
//            Reflector.setIDFieldValue(object, reference.getId());
//            batch = batch.create(reference, object);
//            numOfOperations++;
//        } catch (IllegalAccessException | ClassRegistrationException | NoSuchFieldException e) {
//            throw new BatchException(e);
//        }
//    }
//
//    /**
//     * Updates an object as part of a batch write.
//     * @param object The object to update.
//     * @throws BatchException Thrown when the batch execution encounters an error.
//     */
//    public final void update(final Object object) throws BatchException {
//        try {
//            FS.checkRegistration(object);
//            final String id = Reflector.getIDFieldValue(object);
//            final DocumentReference reference = FS.firestore.collection(object.getClass().getSimpleName()).document(id);
//            batch = batch.set(reference, object);
//            numOfOperations++;
//        } catch (IllegalAccessException | ClassRegistrationException | NoSuchFieldException e) {
//            throw new BatchException(e);
//        }
//    }
//
//    /**
//     * Deletes an object as part of a batch write.
//     * @param object The object to delete.
//     * @throws BatchException Thrown when the batch execution encounters an error.
//     */
//    public final void delete(final Object object) throws BatchException {
//        try {
//            FS.checkRegistration(object);
//            final String id = Reflector.getIDFieldValue(object);
//            final DocumentReference reference = FS.firestore.collection(object.getClass().getSimpleName()).document(id);
//            batch = batch.delete(reference);
//            Reflector.setIDFieldValue(object, null);
//            numOfOperations++;
//        } catch (IllegalAccessException | ClassRegistrationException | NoSuchFieldException e) {
//            throw new BatchException(e);
//        }
//    }
//
//    /**
//     * Deletes an object using an ID.
//     * @param objectClass The class of the object.
//     * @param objectID The object's ID.
//     * @throws BatchException Thrown when the batch execution encounters an error.
//     */
//    public final void delete(final Class<?> objectClass, final String objectID) throws BatchException {
//        try {
//            FS.checkRegistration(objectClass);
//            final DocumentReference reference = FS.firestore.collection(objectClass.getSimpleName()).document(objectID);
//            batch = batch.delete(reference);
//            numOfOperations++;
//        } catch (ClassRegistrationException e) {
//            throw new BatchException(e);
//        }
//    }
//
//    /**
//     * Performs a batch operation.
//     */
//    void doBatch() {
//        if (numOfOperations > 500) {
//            throw new TooManyOperationsException("The number of operations in a batch write cannot exceed 500.");
//        }
//
//        try {
//            managedExecute();
//            final ApiFuture<List<WriteResult>> commitResult = batch.commit();
//            commitResult.get();
//        } catch (InterruptedException | ExecutionException | FirestormException e) {
//            onFailure(e);
//            return;
//        }
//
//        onSuccess();
//    }
//
//}
