package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.exception.FirestormObjectException;
import com.raylabz.firestorm.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Enables Firestore transactions.
 */
public abstract class FirestormTransaction extends FirestormOperation implements Transaction.Function<Void> {

    private Transaction transaction;

    /**
     * Creates a Firestore document from an object as part of a transaction.
     * @param object The object containing the data.
     */
    public final void create(final Object object) {
        try {
            Reflector.checkObject(object);
            final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document();
            Reflector.setIDField(object, reference.getId());
            transaction = transaction.set(reference, object);
        } catch (FirestormObjectException | IllegalAccessException | NoSuchFieldException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Retrieves an object using its documentID as part of a transaction.
     * @param objectClass The class of the object.
     * @param documentID The object's document ID.
     * @param <T> The type of the object (same with objectClass).
     * @return Returns an object of type T/objectClass
     */
    public final <T> T get(final Class<T> objectClass, final String documentID) {
        final DocumentReference documentReference = Firestorm.firestore.collection(objectClass.getSimpleName()).document(documentID);
        try {
            DocumentSnapshot snapshot = transaction.get(documentReference).get();
            return snapshot.toObject(objectClass);
        } catch (InterruptedException | ExecutionException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Updates an object as part of a transaction.
     * @param object The object to update.
     */
    public final void update(final Object object) {
        try {
            Reflector.checkObject(object);
            final String id = Reflector.getIDField(object);
            final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document(id);
            transaction = transaction.set(reference, object);
        } catch (IllegalAccessException | NoSuchFieldException | FirestormObjectException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Deletes an object as part of a transaction.
     * @param object The object to delete.
     */
    public final void delete(final Object object) {
        try {
            Reflector.checkObject(object);
            final String id = Reflector.getIDField(object);
            final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document(id);
            transaction = transaction.delete(reference);
            Reflector.setIDField(object, null);
        } catch (IllegalAccessException | FirestormObjectException | NoSuchFieldException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Deletes an object as part of a transaction.
     * @param objectClass The class of the object to delete.
     * @param objectID The ID of the object to delete.
     * @param <T> The type of the object to delete.
     */
    public <T> void delete(final Class<T> objectClass, final String objectID) {
        final DocumentReference reference = Firestorm.firestore.collection(objectClass.getSimpleName()).document(objectID);
        transaction = transaction.delete(reference);
    }

    /**
     * Lists all objects of type <b>objectClass</b> as part of a transaction.
     * @param objectClass The class of the object.
     * @param limit The maximum number of objects return.
     * @param <T> The type of the object (same with objectClass).
     * @return Returns an ArrayList of type T/objectClass.
     */
    public final <T> ArrayList<T> list(final Class<T> objectClass, final int limit) {
        ApiFuture<QuerySnapshot> future = Firestorm.firestore.collection(objectClass.getSimpleName()).limit(limit).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            final int NUM_OF_DOCUMENTS = documents.size();
            DocumentReference[] documentReferences = new DocumentReference[NUM_OF_DOCUMENTS];
            for (int i = 0; i < documents.size(); i++) {
                documentReferences[i] = documents.get(i).getReference();
            }

            final ApiFuture<List<DocumentSnapshot>> all = transaction.getAll(documentReferences);
            final List<DocumentSnapshot> documentSnapshots = all.get();

            ArrayList<T> documentList = new ArrayList<>();
            for (final DocumentSnapshot snapshot : documentSnapshots) {
                T object = snapshot.toObject(objectClass);
                documentList.add(object);
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new TransactionException(e);
        }
    }

    /**
     * Lists ALL available documents of a given type. May incur charges for read operations for huge numbers of documents.
     *
     * @param objectClass The type of the documents to filter.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an ArrayList of objects of type objectClass.
     */
    public final <T> ArrayList<T> listAll(final Class<T> objectClass) {
        ApiFuture<QuerySnapshot> future = Firestorm.firestore.collection(objectClass.getSimpleName()).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            final int NUM_OF_DOCUMENTS = documents.size();
            DocumentReference[] documentReferences = new DocumentReference[NUM_OF_DOCUMENTS];
            for (int i = 0; i < documents.size(); i++) {
                documentReferences[i] = documents.get(i).getReference();
            }

            final ApiFuture<List<DocumentSnapshot>> all = transaction.getAll(documentReferences);
            final List<DocumentSnapshot> documentSnapshots = all.get();

            ArrayList<T> documentList = new ArrayList<>();
            for (final DocumentSnapshot snapshot : documentSnapshots) {
                T object = snapshot.toObject(objectClass);
                documentList.add(object);
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new TransactionException(e);
        }
    }

    /**
     * Filters objects of a certain type based on given conditions.
     * @param objectClass The class of the object.
     * @param <T> The type of the object (same with objectClass).
     * @return Returns an ArrayList of objects of type T/objectClass, matching the provided filters.
     */
    public final <T> TransactionFilterable<T> filter(final Class<T> objectClass) {
        return new TransactionFilterable<>(Firestorm.firestore.collection(objectClass.getSimpleName()), objectClass, transaction);
    }

    /**
     * Overrides method <b>updateCallback()</b> of the Transaction.Function interface.
     * Initializes the transaction object used to carry out the transaction and then executes the transaction code
     * provided by the developer.
     * @param transaction The transaction object.
     * @return Returns null.
     */
    @Override
    public final Void updateCallback(Transaction transaction) {
        this.transaction = transaction;
        try {
            managedExecute();
        } catch (FirestormException e) {
            onFailure(e);
            return null;
        }
        return null;
    }

}
