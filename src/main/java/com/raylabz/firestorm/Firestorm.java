package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.raylabz.firestorm.exception.FirestormObjectException;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.exception.NotInitializedException;

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
//        firestore.listCollections();
    }

    /**
     * Private constructor.
     */
    private Firestorm() { }

    /**
     * Creates a Firestore document from an object.
     *
     * @param object An object containing the data to be written in Firestore.
     * @param onFailureListener FailureListener to execute onFailure().
     * @return Returns the document ID of the created document.
     */
    public static String create(final Object object, final OnFailureListener onFailureListener) {
        try {
            Reflector.checkObject(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document();
            Reflector.setIDField(object, reference.getId());
            reference.set(object).get();
            return reference.getId();
        } catch (InterruptedException | ExecutionException | FirestormObjectException | NoSuchFieldException | IllegalAccessException e) {
            onFailureListener.onFailure(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
        return null;
    }

    /**
     * Creates a Firestore document from an object.
     *
     * @param object An object containing the data to be written in Firestore.
     * @return Returns the document ID of the created document.
     */
    public static String create(final Object object) {
        try {
            Reflector.checkObject(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document();
            Reflector.setIDField(object, reference.getId());
            reference.set(object).get();
            return reference.getId();
        } catch (InterruptedException | ExecutionException | FirestormObjectException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Retrieves a document as an object from Firestore.
     *
     * @param objectClass The class of the object retrieved.
     * @param documentID  The documentID of the object to retrieve.
     * @param <T>         A type matching the type of objectClass.
     * @param onFailureListener FailureListener to execute onFailure().
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
        } catch (NullPointerException e) {
            throw new NotInitializedException();
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
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

//    /**
//     * Updates a document in Firestore based on the document ID and data of a managed object.
//     *
//     * @param object An object which provides data and the document ID for the update.
//     * @param onFailureListener FailureListener to execute onFailure().
//     */
//    public static void update(final FirestormObject object, final OnFailureListener onFailureListener) {
//        final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(object.getId());
//        try {
//            reference.set(object).get();
//        } catch (InterruptedException | ExecutionException e) {
//            onFailureListener.onFailure(e);
//        }
//    }
//
//    /**
//     * Updates a document in Firestore based on the document ID and data of a managed object.
//     *
//     * @param object An object which provides data and the document ID for the update.
//     */
//    public static void update(final FirestormObject object) {
//        final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(object.getId());
//        try {
//            reference.set(object).get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new FirestormException(e);
//        }
//    }

    /**
     * Updates a document in Firestore.
     *
     * @param object An object which provides data and the document ID for the update.
     * @param onFailureListener FailureListener to execute onFailure().
     */
    public static void update(final Object object, final OnFailureListener onFailureListener) {
        try {
            Reflector.checkObject(object);
            final String documentID = Reflector.getIDField(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            reference.set(object).get();
        } catch (InterruptedException | ExecutionException | FirestormObjectException | IllegalAccessException | NoSuchFieldException e) {
            onFailureListener.onFailure(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Updates a document in Firestore.
     *
     * @param object An object which provides data and the document ID for the update.
     */
    public static void update(final Object object) {
        try {
            Reflector.checkObject(object);
            final String documentID = Reflector.getIDField(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            reference.set(object).get();
        } catch (InterruptedException | ExecutionException | FirestormObjectException | IllegalAccessException | NoSuchFieldException e) {
            throw new FirestormException(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Deletes an object from Firestore.
     * @param objectClass The class of the object to delete.
     * @param objectID The ID of the object/document in Firestore.
     * @param <T> The type (class) of the object.
     */
    public static <T> void delete(final Class<T> objectClass, final String objectID) {
        final DocumentReference reference = firestore.collection(objectClass.getSimpleName()).document(objectID);
        try {
            reference.delete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Deletes an object from Firestore.
     * @param objectClass The class of the object to delete.
     * @param objectID The ID of the object/document in Firestore.
     * @param <T> The type (class) of the object.
     * @param onFailureListener OnFailureListener to execute onFailure().
     */
    public static <T> void delete(final Class<T> objectClass, final String objectID, final OnFailureListener onFailureListener) {
        final DocumentReference reference = firestore.collection(objectClass.getSimpleName()).document(objectID);
        try {
            reference.delete().get();
        } catch (InterruptedException | ExecutionException e) {
            onFailureListener.onFailure(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Deletes an object from Firestore.
     * @param object The object to delete.
     * @param <T> The type (class) of the object.
     */
    public static <T> void delete(final Object object) {
        try {
            Reflector.checkObject(object);
            final String documentID = Reflector.getIDField(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            reference.delete().get();
            Reflector.setIDField(object, null);
        } catch (InterruptedException | ExecutionException | IllegalAccessException | NoSuchFieldException | FirestormObjectException e) {
            throw new FirestormException(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Deletes an object from Firestore.
     * @param object The object to delete.
     * @param onFailureListener OnFailureListener to execute onFailure().
     * @param <T> The type (class) of the object.
     */
    public static <T> void delete(final Object object, final OnFailureListener onFailureListener) {
        try {
            Reflector.checkObject(object);
            final String documentID = Reflector.getIDField(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            reference.delete().get();
            Reflector.setIDField(object, null);
        } catch (InterruptedException | ExecutionException | IllegalAccessException | NoSuchFieldException | FirestormObjectException e) {
            onFailureListener.onFailure(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Lists available documents of a given type.
     *
     * @param objectClass The type of the documents to filter.
     * @param limit       The maximum number of objects to return.
     * @param <T>         A type matching the type of objectClass.
     * @param onFailureListener OnFailureListener to execute onFailure().
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
        } catch (NullPointerException e) {
            throw new NotInitializedException();
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
    public static <T> ArrayList<T> list(final Class<T> objectClass, final int limit) {
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
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Lists ALL available documents of a given type. May incur charges for read operations for huge numbers of documents.
     *
     * @param objectClass The type of the documents to filter.
     * @param <T>         A type matching the type of objectClass.
     * @param onFailureListener OnFailureListener to execute onFailure().
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
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Lists ALL available documents of a given type. May incur charges for read operations for huge numbers of documents.
     *
     * @param objectClass The type of the documents to filter.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an ArrayList of objects of type objectClass.
     */
    public static <T> ArrayList<T> listAll(final Class<T> objectClass) {
        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            for (final QueryDocumentSnapshot document : documents) {
                documentList.add(document.toObject(objectClass));
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            throw new FirestormException(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
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
     * Retrieves a DocumentReference to an object.
     *
     * @param objectClass The type of the object.
     * @param documentID The object's ID.
     * @param <T> The type of the object.
     * @return Returns DocumentReference.
     */
    public static <T> DocumentReference getObjectReference(final Class<T> objectClass, final String documentID) {
        return firestore.collection(objectClass.getSimpleName()).document(documentID);
    }

    public static <T> DocumentReference getObjectReference(Object object) {
        try {
            Reflector.checkObject(object);
            final String documentID = Reflector.getIDField(object);
            return firestore.collection(object.getClass().getSimpleName()).document(documentID);
        } catch (IllegalAccessException | NoSuchFieldException | FirestormObjectException e) {
            throw new FirestormException(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Retrieves a CollectionReference to a type.
     *
     * @param objectClass The class of object to get a reference for.
     * @param <T> The Type of class.
     * @return Returns a CollectionReference.
     */
    public static <T> CollectionReference getCollectionReference(final Class<T> objectClass) {
        return firestore.collection(objectClass.getSimpleName());
    }

    /**
     * Attaches an event listener which listens for updates to an object.
     *
     * @param eventListener An implementation of a FirestormEventListener.
     * @return Returns a ListenerRegistration.
     */
    public static ListenerRegistration attachListener(final OnObjectUpdateListener eventListener) {
        try {
            Reflector.checkObject(eventListener.getObjectToListenFor());
            final String documentID = Reflector.getIDField(eventListener.getObjectToListenFor());
            return firestore.collection(eventListener.getObjectToListenFor().getClass().getSimpleName()).document(documentID).addSnapshotListener(eventListener);
        } catch (FirestormObjectException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        } catch (NullPointerException e) {
            throw new NotInitializedException();
        }
    }

    /**
     * Attaches an event listener which listens for updates to an object using its class and ID.
     *
     * @param eventListener An implementation of a FirestormEventListener.
     * @return Returns a ListenerRegistration.
     */
    public static ListenerRegistration attachListener(final OnReferenceUpdateListener eventListener) {
        return firestore.collection(eventListener.getObjectClass().getSimpleName()).document(eventListener.getDocumentID()).addSnapshotListener(eventListener);
    }

    /**
     * Detaches a specified listener from an object.
     *
     * @param listenerRegistration The listenerRegistration to detach.
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
        } catch (NullPointerException e) {
            throw new NotInitializedException();
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
