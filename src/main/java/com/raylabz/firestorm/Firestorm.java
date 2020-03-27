package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * The main class of the Firestorm API, which allows interactions with the Firestore.
 * Must be initialized with a FirebaseApp object using the <b>init()</b> method before interacting with the Firestore.
 */
public class Firestorm {

    private static Firestorm instance = null;
    private static FirebaseOptions options;
    private static FirebaseApp firebaseApp;
    public static Firestore firestore; //TODO package private

    /**
     * Retrieves instance of Firestorm.
     * @return Returns Firestorm instance.
     */
    public static Firestorm getInstance() {
        return instance;
    }

    /**
     * Initializes Firestorm with an instance of a FirebaseApp.
     * @param firebaseApp An <u>initialized</u> FirebaseApp object.
     */
    public static void init(final FirebaseApp firebaseApp) {
        Firestorm.firebaseApp = firebaseApp;
        Firestorm.firestore = FirestoreClient.getFirestore();
        //TODO Consider initialization of connection to improve first-connection latency?
    }

    /**
     * Private constructor.
     */
    private Firestorm() { }

    /**
     * Creates a Firestore document from an object.
     * @param object An object containing the data to be written in Firestore.
     * @return Returns <b>true</b> if successful, false otherwise.
     */
    public static boolean create(final FirestormObject object) {
        final String className = object.getClass().getSimpleName();
        final DocumentReference reference = firestore.collection(className).document();
        final String id = reference.getId();
        try {
            object.setId(id);
            reference.set(object).get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a document from the Firestore based on the document ID of an object.
     * @param object An object which provides the document ID for deletion.
     * @return Returns <b>true</b> if successful, false otherwise.
     */
    public static boolean delete(final FirestormObject object) {
        final String className = object.getClass().getSimpleName();
        final DocumentReference reference = firestore.collection(className).document(object.getId());
        try {
            reference.delete().get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a document in Firestore based on the document ID and data of an object.
     * @param object An object which provides data and the document ID for the update.
     * @return Returns <b>true</b> if successful, false otherwise.
     */
    public static boolean update(final FirestormObject object) {
        final String className = object.getClass().getSimpleName();
        final DocumentReference reference = firestore.collection(className).document(object.getId());
        try {
            reference.set(object).get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a document as an object from Firestore.
     * @param aClass The class of the object retrieved.
     * @param documentID The documentID of the object to retrieve.
     * @param <T> A type matching the type of aClass.
     * @return Returns an object of type T (aClass).
     */
    public static <T> T get(final Class<T> aClass, final String documentID) {
        DocumentReference docRef = firestore.collection(aClass.getSimpleName()).document(documentID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
            if (document.exists()) {
                return (T) document.toObject(aClass);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lists all available documents of a given type.
     * @param aClass The type of the documents to filter.
     * @param <T> A type matching the type of aClass.
     * @return Returns an ArrayList of objects of type aClass.
     */
    public static <T> ArrayList<T> list(final Class<T> aClass) {
        ApiFuture<QuerySnapshot> future = firestore.collection(aClass.getSimpleName()).get();
        try {
            List<QueryDocumentSnapshot> documents = null;
            documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            for (final QueryDocumentSnapshot document : documents) {
                documentList.add((T) document.toObject(aClass));
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lists a set documents which match the filtering criteria provided. Returns a filter of all documents if not filters are used.
     * @param aClass The type of the documents to filter.
     * @param <T> A type matching the type of aClass.
     * @return Returns a FirestormFilterable which can be used to append filter parameters.
     */
    public static <T> FirestormFilterable<T> filter(final Class<T> aClass) {
        return new FirestormFilterable<T>(firestore.collection(aClass.getSimpleName()), aClass);
    }

    /**
     * Retrieves a DocumentReference to an object.
     * @param object The object to retrieve the DocumentReference for.
     * @return Returns DocumentReference.
     */
    public static DocumentReference getObjectReference(final FirestormObject object) {
        return firestore.collection(object.getClass().getSimpleName()).document(object.getId());
    }

    public static CollectionReference getFieldReference(final FirestormObject object, final String fieldName) {
        return firestore.collection(object.getClass().getSimpleName()).document(object.getId()).collection(fieldName);
    }

    /**
     * Attaches an event listener which listens for updates to an object.
     * @param object The object to attach the listener to.
     * @param eventListener An implementation of a FirestormEventListener.
     * @param <T> The type of objects this listener can be attached to.
     */
    public static <T> void attachListener(final FirestormObject object, final FirestormEventListener<T> eventListener) {
        ListenerRegistration listenerRegistration = getObjectReference(object).addSnapshotListener(eventListener);
        object.addListener(listenerRegistration);
    }

    /**
     * Detaches a specified listener from an object.
     * @param object The object to detach the listener from.
     * @param listenerRegistration The listener.
     */
    public static void detachListener(final FirestormObject object, ListenerRegistration listenerRegistration) {
        listenerRegistration.remove();
        object.removeListener(listenerRegistration);
    }

    /**
     * Detaches all listeners from an object.
     * @param object The object to detach the listeners from.
     */
    public static void detachAllListeners(final FirestormObject object) {
        for (ListenerRegistration listenerRegistration : object.getListeners()) {
            listenerRegistration.remove();
            object.removeListener(listenerRegistration);
        }
    }

    /**
     * Runs a transaction.
     * @param transaction The transaction to run.
     */
    public static void runTransaction(final FirestormTransaction transaction) {
        ApiFuture<Void> futureTransaction = Firestorm.firestore.runTransaction(transaction);
        try {
            futureTransaction.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }


}
