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
    public static void create(final FirestormObject object) {
        final String className = object.getClass().getSimpleName();
        final DocumentReference reference = firestore.collection(className).document();
        final String id = reference.getId();
        try {
            object.setId(id);
            reference.set(object).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes a document from the Firestore based on the document ID of an object.
     *
     * @param object An object which provides the document ID for deletion.
     */
    public static void delete(final FirestormObject object) {
        final String className = object.getClass().getSimpleName();
        final DocumentReference reference = firestore.collection(className).document(object.getId());
        try {
            reference.delete().get();
            object.setId(null);
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Updates a document in Firestore based on the document ID and data of an object.
     *
     * @param object An object which provides data and the document ID for the update.
     */
    public static void update(final FirestormObject object) {
        final String className = object.getClass().getSimpleName();
        final DocumentReference reference = firestore.collection(className).document(object.getId());
        try {
            reference.set(object).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
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
    public static <T> T get(final Class<T> objectClass, final String documentID) {
        DocumentReference docRef = firestore.collection(objectClass.getSimpleName()).document(documentID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(objectClass);
            } else {
                throw new FirestormException("The document with ID " + documentID + " does not exist.");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Lists available documents of a given type.
     *
     * @param objectClass The type of the documents to filter.
     * @param limit       The maximum number of documents to return.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an ArrayList of objects of type objectClass.
     */
    public static <T> ArrayList<T> list(final Class<T> objectClass, int limit) {
        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).limit(limit).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            for (final QueryDocumentSnapshot document : documents) {
                documentList.add(document.toObject(objectClass));
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Lists ALL available documents of a given type. May incur charges for read operations for huge numbers of documents.
     *
     * @param objectClass The type of the documents to filter.
     * @param <T>         A type matching the type of objectClass.
     * @param getAll      Indicates whether or not to get all documents of this type. If getAll is false, the function
     *                    will retrieve the first 100 documents by default.
     * @return Returns an ArrayList of objects of type objectClass.
     */
    public static <T> ArrayList<T> listAll(final Class<T> objectClass, boolean getAll) {
        ApiFuture<QuerySnapshot> future;
        if (getAll) {
            future = firestore.collection(objectClass.getSimpleName()).get();
        } else {
            future = firestore.collection(objectClass.getSimpleName()).limit(100).get();
        }
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            for (final QueryDocumentSnapshot document : documents) {
                documentList.add(document.toObject(objectClass));
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
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
        return new FirestormFilterable(firestore.collection(objectClass.getSimpleName()), objectClass);
    }

    /**
     * Retrieves a DocumentReference to an object.
     *
     * @param object The object to retrieve the DocumentReference for.
     * @return Returns DocumentReference.
     */
    public static DocumentReference getObjectReference(final FirestormObject object) {
        return firestore.collection(object.getClass().getSimpleName()).document(object.getId());
    }

    /**
     * Attaches an event listener which listens for updates to an object.
     *
     * @param object        The object to attach the listener to.
     * @param eventListener An implementation of a FirestormEventListener.
     * @param <T>           The type of objects this listener can be attached to.
     */
    public static <T> void attachListener(final FirestormObject object, final FirestormEventListener<T> eventListener) {
        ListenerRegistration listenerRegistration = getObjectReference(object).addSnapshotListener(eventListener);
        object.addListener(listenerRegistration);
    }

    /**
     * Detaches a specified listener from an object.
     *
     * @param object               The object to detach the listener from.
     * @param listenerRegistration The listener.
     */
    public static void detachListener(final FirestormObject object, ListenerRegistration listenerRegistration) {
        listenerRegistration.remove();
        object.removeListener(listenerRegistration);
    }

    /**
     * Detaches all listeners from an object.
     *
     * @param object The object to detach the listeners from.
     */
    public static void detachAllListeners(final FirestormObject object) {
        for (ListenerRegistration listenerRegistration : object.getListeners()) {
            listenerRegistration.remove();
            object.removeListener(listenerRegistration);
        }
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
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
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
