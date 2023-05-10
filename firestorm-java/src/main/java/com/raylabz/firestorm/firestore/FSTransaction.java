package com.raylabz.firestorm.firestore;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.api.BasicReadOperationProvider;
import com.raylabz.firestorm.api.BasicWriteOperationProvider;
import com.raylabz.firestorm.api.FirestormOperation;
import com.raylabz.firestorm.exception.ClassRegistrationException;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.exception.IDFieldException;
import com.raylabz.firestorm.exception.TransactionException;
import com.raylabz.firestorm.util.Reflector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Enables Firestore transactions.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public abstract class FSTransaction extends FirestormOperation
        implements Transaction.Function<Void>,
        BasicWriteOperationProvider<Void, TransactionException>,
        BasicReadOperationProvider<TransactionException> {

    private Transaction transaction;

    /**
     * Creates a Firestore document from an object as part of a transaction.
     * @param object The object containing the data.
     * @throws TransactionException Thrown when the transaction encounters an error.
     */
    public final Void create(final Object object) throws TransactionException {
        try {
            Firestorm.checkRegistration(object);
            String idFieldValue = Reflector.getIDFieldValue(object);
            if (idFieldValue == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = FS.getInstance().collection(object.getClass().getSimpleName()).document(idFieldValue);
            transaction = transaction.set(reference, object);
        } catch (ClassRegistrationException | IllegalAccessException | NoSuchFieldException | IDFieldException e) {
            throw new TransactionException(e);
        }
        return null;
    }

    /**
     * Retrieves an object using its documentID as part of a transaction.
     * @param objectClass The class of the object.
     * @param documentID The object's document ID.
     * @param <T> The type of the object (same with objectClass).
     * @return Returns an object of type T/objectClass.
     * @throws TransactionException Thrown when the transaction encounters an error.
     */
    public final <T> T get(final Class<T> objectClass, final String documentID) throws TransactionException {
        try {
            final DocumentReference documentReference = FS.getInstance().collection(objectClass.getSimpleName()).document(documentID);
            DocumentSnapshot snapshot = transaction.get(documentReference).get();
            if (snapshot.exists()) {
                return snapshot.toObject(objectClass);
            }
            else {
                throw new TransactionException(new RuntimeException("No object exists for reference: " + documentReference.getId()));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Updates an object as part of a transaction.
     * @param object The object to update.
     * @throws TransactionException Thrown when the transaction encounters an error.
     */
    public final Void update(final Object object) throws TransactionException {
        try {
            Firestorm.checkRegistration(object);
            String idFieldValue = Reflector.getIDFieldValue(object);
            if (idFieldValue == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = FS.getInstance().collection(object.getClass().getSimpleName()).document(idFieldValue);
            transaction = transaction.set(reference, object);
        } catch (IllegalAccessException | NoSuchFieldException | ClassRegistrationException | IDFieldException e) {
            throw new TransactionException(e);
        }
        return null;
    }

    /**
     * Deletes an object as part of a transaction.
     * @param object The object to delete.
     * @throws TransactionException Thrown when the transaction encounters an error.
     */
    public final Void delete(final Object object) throws TransactionException {
        try {
            Firestorm.checkRegistration(object);
            final String documentID = Reflector.getIDFieldValue(object);
            if (documentID == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = FS.getInstance().collection(object.getClass().getSimpleName()).document(documentID);
            transaction = transaction.delete(reference);
            Reflector.setIDFieldValue(object, null);
        } catch (IllegalAccessException | ClassRegistrationException | NoSuchFieldException | IDFieldException e) {
            throw new TransactionException(e);
        }
        return null;
    }

    /**
     * Deletes an object as part of a transaction.
     * @param objectClass The class of the object to delete.
     * @param objectID The ID of the object to delete.
     * @param <T> The type of the object to delete.
     * @throws TransactionException Thrown when the transaction encounters an error.
     */
    public <T> Void delete(final Class<T> objectClass, final String objectID) throws TransactionException {
        try {
            Firestorm.checkRegistration(objectClass);
            final DocumentReference reference = FS.getInstance().collection(objectClass.getSimpleName()).document(objectID);
            transaction = transaction.delete(reference);
        } catch (ClassRegistrationException e) {
            throw new TransactionException(e);
        }
        return null;
    }

    /**
     * Lists all objects of type <b>objectClass</b> as part of a transaction.
     * @param objectClass The class of the object.
     * @param limit The maximum number of objects return.
     * @param <T> The type of the object (same with objectClass).
     * @return Returns an ArrayList of type T/objectClass.
     * @throws TransactionException Thrown when the transaction encounters an error.
     */
    public final <T> List<T> list(final Class<T> objectClass, final int limit) throws TransactionException {
        try {
            Firestorm.checkRegistration(objectClass);
            ApiFuture<QuerySnapshot> future = FS.getInstance().collection(objectClass.getSimpleName()).limit(limit).get();
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
        } catch (InterruptedException | ExecutionException | ClassRegistrationException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Lists ALL available documents of a given type. May incur charges for read operations for huge numbers of documents.
     *
     * @param objectClass The type of the documents to filter.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an ArrayList of objects of type objectClass.
     * @throws TransactionException Thrown when the transaction encounters an error.
     */
    public final <T> ArrayList<T> list(final Class<T> objectClass) throws TransactionException {
        try {
            Firestorm.checkRegistration(objectClass);
            ApiFuture<QuerySnapshot> future = FS.getInstance().collection(objectClass.getSimpleName()).get();
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
        } catch (InterruptedException | ExecutionException | ClassRegistrationException e) {
            throw new TransactionException(e);
        }
    }

    /**
     * Filters objects of a certain type based on given conditions.
     * @param objectClass The class of the object.
     * @param <T> The type of the object (same with objectClass).
     * @return Returns an ArrayList of objects of type T/objectClass, matching the provided filters.
     */
    public final <T> FSTransactionFilterable<T> filter(final Class<T> objectClass) {
        return new FSTransactionFilterable<>(FS.getInstance().collection(objectClass.getSimpleName()), objectClass, transaction);
    }

    /**
     * Overrides method <b>updateCallback()</b> of the Transaction.Function interface.
     * Initializes the transaction object used to carry out the transaction and then executes the transaction code
     * provided.
     * @param transaction The transaction object.
     * @return Returns null.
     */
    @Override
    public final Void updateCallback(Transaction transaction) {
        this.transaction = transaction;
        managedExecute();
        return null;
    }

}
