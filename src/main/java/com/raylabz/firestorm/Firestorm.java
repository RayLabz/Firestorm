package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/*
 * Firestorm is an object-oriented data access API for Firestore.
 * https://raylabz.github.io/Firestorm/
 * @version 1.0.0
 *
 * @author RayLabz 2020
 * https://www.raylabz.com
 */

/**
 * The main class of the Firestorm API, which allows basic interactions with the Firestore.
 * Must be initialized with a FirebaseApp object using the <i>init()</i> method before interacting with the Firestore.
 */
public class Firestorm {

    static Firestore firestore;

    /**
     * Initializes Firestorm <b><u>after Firebase has been initialized</u></b> using <i>Firebase.initializeApp()</i>.
     */
    public static void init() {
        firestore = FirestoreClient.getFirestore();

        /* Dummy request used for initialization:
            The initial call to Firestore has high latency so it is preferable to make a dummy request
            as soon as the object is initialized instead of waiting to make the first connection when a real request is made.
         */
        firestore.listCollections();
    }

    /**
     * Private constructor.
     */
    private Firestorm() { }

    /**
     * Creates a Firestore document from an object.
     *
     * @param object An object containing the data to be written in Firestore.
     */
    public static void create(final FirestormObject object, final OnFailureListener onFailureListener) {
        final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document();
        try {
            object.setId(reference.getId());
            reference.set(object).get();
        } catch (InterruptedException | ExecutionException e) {
            onFailureListener.onFailure(e);
        }
    }

    /**
     * Retrieves a document as an object from Firestore.
     *
     * @param objectClass The class of the object retrieved.
     * @param documentID  The documentID of the object to retrieve.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an object of type T (objectClass).
     */
    public static <T> T get(final Class<T> objectClass, final String documentID, final OnFailureListener onFailureListener) {
        DocumentReference docRef = firestore.collection(objectClass.getSimpleName()).document(documentID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(objectClass);
            } else {
                onFailureListener.onFailure(new FirestormException("The document with ID " + documentID + " does not exist."));
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            onFailureListener.onFailure(e);
            return null;
        }
    }

    /**
     * Updates a document in Firestore based on the document ID and data of an object.
     *
     * @param object An object which provides data and the document ID for the update.
     */
    public static void update(final FirestormObject object, final OnFailureListener onFailureListener) {
        final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(object.getId());
        try {
            reference.set(object).get();
        } catch (InterruptedException | ExecutionException e) {
            onFailureListener.onFailure(e);
        }
    }

    /**
     * Deletes a document from the Firestore based on the document ID of an object.
     *
     * @param object An object which provides the document ID for deletion.
     */
    public static void delete(final FirestormObject object, final OnFailureListener onFailureListener) {
        final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(object.getId());
        try {
            reference.delete().get();
            object.setId(null);
        } catch (InterruptedException | ExecutionException e) {
            onFailureListener.onFailure(e);
        }
    }

    /**
     * Lists available documents of a given type.
     *
     * @param objectClass The type of the documents to filter.
     * @param limit       The maximum number of objects to return.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an ArrayList of objects of type objectClass.
     */
    public static <T> ArrayList<T> list(final Class<T> objectClass, final int limit, final OnFailureListener onFailureListener) {
        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).limit(limit).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            for (final QueryDocumentSnapshot document : documents) {
                documentList.add(document.toObject(objectClass));
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            onFailureListener.onFailure(e);
            return null;
        }
    }

    /**
     * Lists ALL available documents of a given type. May incur charges for read operations for huge numbers of documents.
     *
     * @param objectClass The type of the documents to filter.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an ArrayList of objects of type objectClass.
     */
    public static <T> ArrayList<T> listAll(final Class<T> objectClass, final OnFailureListener onFailureListener) {
        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            for (final QueryDocumentSnapshot document : documents) {
                documentList.add(document.toObject(objectClass));
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            onFailureListener.onFailure(e);
            return null;
        }
    }

    /**
     * Lists a set documents which match the filtering criteria provided. Returns a filter of all documents if no filters are used.
     *
     * @param objectClass The type of the documents to filter.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns a FirestormFilterable which can be used to append filter parameters.
     */
    public static <T> FirestormFilterable<T> filter(final Class<T> objectClass) {
        return new FirestormFilterable<T>(firestore.collection(objectClass.getSimpleName()), objectClass);
    }

    /**
     * Retrieves a DocumentReference to an object using an ID.
     * @param objectClass The type of the object.
     * @param documentID The object's ID.
     * @param <T> The type of the object.
     * @return Returns DocumentReference.
     */
    public static <T> DocumentReference getObjectReference(final Class<T> objectClass, final String documentID) {
        return firestore.collection(objectClass.getSimpleName()).document(documentID);
    }

    /**
     * Retrieves a CollectionReference to a type.
     * @param objectClass The class of object to get a reference for.
     * @param <T> The Type of class.
     * @return Returns a CollectionReference.
     */
    public static <T> CollectionReference getCollectionReference(final Class<T> objectClass) {
        return firestore.collection(objectClass.getSimpleName());
    }

    /**
     * Attaches an event listener which listens for updates to an object using its ID.
     * @param objectClass The class of the object.
     * @param documentID The object's ID.
     * @param eventListener An implementation of a FirestormEventListener.
     * @param <T> The Type of objects this listener can be attached to.
     * @return Returns a ListenerRegistration.
     */
    public static <T> ListenerRegistration attachListener(final Class<T> objectClass, final String documentID, final FirestormEventListener<T> eventListener) {
        return firestore.collection(objectClass.getSimpleName()).document(documentID).addSnapshotListener(eventListener);
    }

    /**
     * Detaches a specified listener from an object.
     * @param listenerRegistration The listenerRegistration to detatch.
     */
    public static void detachListener(ListenerRegistration listenerRegistration) {
        listenerRegistration.remove();
    }

    /**
     * Runs a transaction operation.
     *
     * @param transaction The transaction to run.
     */
    public static void runTransaction(final FirestormTransaction transaction) {
        ApiFuture<Void> futureTransaction = Firestorm.firestore.runTransaction(transaction);
        try {
            futureTransaction.get();
            transaction.onSuccess();
        } catch (InterruptedException | ExecutionException e) {
            transaction.onFailure(e);
        }
    }

    /**
     * Runs a batch write operation.
     *
     * @param batch The batch to run.
     */
    public static void runBatch(final FirestormBatch batch) {
        batch.doBatch();
    }

}
