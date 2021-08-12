package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.raylabz.firestorm.exception.ClassRegistrationException;
import com.raylabz.firestorm.exception.FirestormObjectException;
import com.raylabz.firestorm.exception.FirestormException;
import com.raylabz.firestorm.exception.NotInitializedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Firestorm is an object-oriented data access API for Firestore.
 * https://raylabz.github.io/Firestorm/.
 * This is the main class of the Firestorm API, which allows basic interactions with the Firestore.
 * Must be initialized with a FirebaseApp object using the <i>init()</i> method before interacting with the Firestore.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public final class Firestorm {

    /**
     * Static Firestore object used to make queries on Firestore.
     */
    static Firestore firestore;

    /**
     * Stores a list of listeners registered to objects.
     */
    private static final HashMap<Object, ListenerRegistration> registeredListeners = new HashMap<>();

    /**
     * Initializes Firestorm <b><u>after Firebase has been initialized</u></b> using <i>Firebase.initializeApp()</i>.
     */
    public static void init() {
        firestore = FirestoreClient.getFirestore();

        /* Dummy request used for initialization:
            The initial call to Firestore has high latency so it is preferable to make a dummy request
            as soon as Firebase is initialized instead of waiting to make the first connection when an actual request is made.
         */
        firestore.listCollections();
    }

    /**
     * Private constructor.
     */
    private Firestorm() { }

    /**
     * Registers a class.
     * @param aClass The class to register.
     * @throws FirestormException Thrown when the class cannot be registered.
     */
    public static void register(Class<?> aClass) throws FirestormException {
        try {
            FirestormRegistry.register(aClass);
        } catch (FirestormObjectException e) {
            throw new FirestormException(e.getMessage());
        }
    }

    /**
     * Checks if an object's class is registered.
     * @param object The object to check the class of.
     * @throws ClassRegistrationException Thrown when the object's class is not registered.
     */
    static void checkRegistration(final Object object) throws ClassRegistrationException {
        if (!FirestormRegistry.isRegistered(object.getClass())) {
            throw new ClassRegistrationException(object.getClass());
        }
    }

    /**
     * Checks if a class is registered.
     * @param aClass The class to check.
     * @throws ClassRegistrationException Thrown when the class is not registered.
     */
    static void checkRegistration(final Class<?> aClass) throws ClassRegistrationException {
        if (!FirestormRegistry.isRegistered(aClass)) {
            throw new ClassRegistrationException(aClass);
        }
    }

    /**
     * Creates a Firestore document from an object.
     *
     * @param object An object containing the data to be written in Firestore.
     * @param onFailureListener FailureListener to execute onFailure().
     * @return Returns the document ID of the created document.
     */
    public static String create(final Object object, final OnFailureListener onFailureListener) {
        try {
            checkRegistration(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document();
            Reflector.setIDField(object, reference.getId());
            reference.set(object).get();
            return reference.getId();
        } catch (InterruptedException | ExecutionException | ClassRegistrationException | NoSuchFieldException | IllegalAccessException | NotInitializedException e) {
            onFailureListener.onFailure(e);
        }
        return null;
    }

    /**
     * Creates a Firestore document from an object.
     *
     * @param object An object containing the data to be written in Firestore.
     * @return Returns the document ID of the created document.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static String create(final Object object) throws FirestormException {
        try {
            checkRegistration(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document();
            Reflector.setIDField(object, reference.getId());
            reference.set(object).get();
            return reference.getId();
        } catch (InterruptedException | ExecutionException | ClassRegistrationException | NoSuchFieldException | IllegalAccessException | NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Creates a Firestore document from an object with a specific ID.
     *
     * @param object An object containing the data to be written in Firestore.
     * @param id The ID of the object to create.
     * @param onFailureListener FailureListener to execute onFailure().
     * @return Returns the document ID of the created document.
     */
    public static String create(final Object object, final String id, final OnFailureListener onFailureListener) {
        try {
            checkRegistration(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(id);
            Reflector.setIDField(object, reference.getId());
            reference.set(object).get();
            return reference.getId();
        } catch (InterruptedException | ExecutionException | ClassRegistrationException | NoSuchFieldException | IllegalAccessException | NotInitializedException e) {
            onFailureListener.onFailure(e);
        }
        return null;
    }

    /**
     * Creates a Firestore document from an object.
     *
     * @param object An object containing the data to be written in Firestore.
     * @param id The ID of the object to create.
     * @return Returns the document ID of the created document.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static String create(final Object object, final String id) throws FirestormException {
        try {
            checkRegistration(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(id);
            Reflector.setIDField(object, reference.getId());
            reference.set(object).get();
            return reference.getId();
        } catch (InterruptedException | ExecutionException | ClassRegistrationException | NoSuchFieldException | IllegalAccessException | NotInitializedException e) {
            throw new FirestormException(e);
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
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
            onFailureListener.onFailure(e);
            return null;
        }
    }

    /**
     * Retrieves multiple documents of a class as a list of objects.
     * @param objectClass The class of the objects.
     * @param ids A list of IDs of the objects to retrieve.
     * @param <T> A type matching the type of object class.
     * @return Returns a list of type T.
     */
    public static <T> List<T> getMany(final Class<T> objectClass, List<String> ids) {
        ArrayList<T> retItems = new ArrayList<>();
        final DocumentReference[] documentReferences = new DocumentReference[ids.size()];
        for (int i = 0; i < documentReferences.length; i++) {
            documentReferences[i] = (firestore.collection(objectClass.getSimpleName()).document(ids.get(i)));
        }
        final ApiFuture<List<DocumentSnapshot>> items = firestore.getAll(documentReferences);
        try {
            final List<DocumentSnapshot> documentSnapshots = items.get();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                if (documentSnapshot.exists()) {
                    retItems.add(documentSnapshot.toObject(objectClass));
                }
            }
            return retItems;
        } catch (ExecutionException | InterruptedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Retrieves multiple documents of a class as a list of objects.
     * @param objectClass The class of the objects.
     * @param ids A list of IDs of the objects to retrieve.
     * @param <T> A type matching the type of object class.
     * @return Returns a list of type T.
     */
    public static <T> List<T> getMany(final Class<T> objectClass, String... ids) {
        ArrayList<T> retItems = new ArrayList<>();
        final DocumentReference[] documentReferences = new DocumentReference[ids.length];
        for (int i = 0; i < documentReferences.length; i++) {
            documentReferences[i] = (firestore.collection(objectClass.getSimpleName()).document(ids[i]));
        }
        final ApiFuture<List<DocumentSnapshot>> items = firestore.getAll(documentReferences);
        try {
            final List<DocumentSnapshot> documentSnapshots = items.get();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                if (documentSnapshot.exists()) {
                    retItems.add(documentSnapshot.toObject(objectClass));
                }
            }
            return retItems;
        } catch (ExecutionException | InterruptedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Retrieves multiple documents of a class as a list of objects.
     * @param objectClass The class of the objects.
     * @param ids A list of IDs of the objects to retrieve.
     * @param <T> A type matching the type of object class.
     * @param onFailureListener FailureListener to execute onFailure().
     * @return Returns a list of type T.
     */
    public static <T> List<T> getMany(final Class<T> objectClass, List<String> ids, OnFailureListener onFailureListener) {
        ArrayList<T> retItems = new ArrayList<>();
        final DocumentReference[] documentReferences = new DocumentReference[ids.size()];
        for (int i = 0; i < documentReferences.length; i++) {
            documentReferences[i] = (firestore.collection(objectClass.getSimpleName()).document(ids.get(i)));
        }
        final ApiFuture<List<DocumentSnapshot>> items = firestore.getAll(documentReferences);
        try {
            final List<DocumentSnapshot> documentSnapshots = items.get();
            for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                if (documentSnapshot.exists()) {
                    retItems.add(documentSnapshot.toObject(objectClass));
                }
            }
            return retItems;
        } catch (ExecutionException | InterruptedException e) {
            onFailureListener.onFailure(e);
            return null;
        }
    }

    /**
     * Retrieves a document as an object from Firestore.
     *
     * @param objectClass The class of the object retrieved.
     * @param documentID  The documentID of the object to retrieve.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an object of type T (objectClass).
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static <T> T get(final Class<T> objectClass, final String documentID) throws FirestormException {
        DocumentReference docRef = firestore.collection(objectClass.getSimpleName()).document(documentID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(objectClass);
            }
            else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Checks if a given documentID of a given class exists.
     *
     * @param objectClass The object class.
     * @param documentID The document ID.
     * @return Returns true if the document exists on Firestore, false otherwise.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static boolean exists(final Class<?> objectClass, final String documentID) throws FirestormException {
        DocumentReference docRef = firestore.collection(objectClass.getSimpleName()).document(documentID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            return document.exists();
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Checks if a given documentID of a given class exists.
     *
     * @param objectClass The object class.
     * @param documentID The document ID.
     * @param onFailureListener A failure listener.
     * @return Returns true if the document exists on Firestore, false otherwise.
     */
    public static boolean exists(final Class<?> objectClass, final String documentID, final OnFailureListener onFailureListener) {
        DocumentReference docRef = firestore.collection(objectClass.getSimpleName()).document(documentID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            return document.exists();
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
            onFailureListener.onFailure(e);
            return false;
        }
    }

    /**
     * Updates a document in Firestore.
     *
     * @param object An object which provides data and the document ID for the update.
     * @param onFailureListener FailureListener to execute onFailure().
     */
    public static void update(final Object object, final OnFailureListener onFailureListener) {
        try {
            checkRegistration(object);
            final String documentID = Reflector.getIDField(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            reference.set(object).get();
        } catch (InterruptedException | ExecutionException | ClassRegistrationException | IllegalAccessException | NoSuchFieldException | NotInitializedException e) {
            onFailureListener.onFailure(e);
        }
    }

    /**
     * Updates a document in Firestore.
     *
     * @param object An object which provides data and the document ID for the update.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static void update(final Object object) throws FirestormException {
        try {
            checkRegistration(object);
            final String documentID = Reflector.getIDField(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            reference.set(object).get();
        } catch (InterruptedException | ExecutionException | ClassRegistrationException | IllegalAccessException | NoSuchFieldException | NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes an object from Firestore.
     *
     * @param objectClass The class of the object to delete.
     * @param objectID The ID of the object/document in Firestore.
     * @param <T> The type (class) of the object.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static <T> void delete(final Class<T> objectClass, final String objectID) throws FirestormException {
        final DocumentReference reference = firestore.collection(objectClass.getSimpleName()).document(objectID);
        try {
            reference.delete().get();
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes an object from Firestore.
     *
     * @param objectClass The class of the object to delete.
     * @param objectID The ID of the object/document in Firestore.
     * @param <T> The type (class) of the object.
     * @param onFailureListener OnFailureListener to execute onFailure().
     * @throws NotInitializedException Thrown when the reference to an object cannot be initialized.
     */
    public static <T> void delete(final Class<T> objectClass, final String objectID, final OnFailureListener onFailureListener) {
        final DocumentReference reference = firestore.collection(objectClass.getSimpleName()).document(objectID);
        try {
            reference.delete().get();
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
            onFailureListener.onFailure(e);
        }
    }

    /**
     * Deletes an object from Firestore.
     *
     * @param object The object to delete.
     * @param <T> The type (class) of the object.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static <T> void delete(final Object object) throws FirestormException {
        try {
            checkRegistration(object);
            final String documentID = Reflector.getIDField(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            reference.delete().get();
            Reflector.setIDField(object, null);
        } catch (InterruptedException | ExecutionException | IllegalAccessException | NoSuchFieldException | ClassRegistrationException | NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes an object from Firestore.
     *
     * @param object The object to delete.
     * @param onFailureListener OnFailureListener to execute onFailure().
     * @param <T> The type (class) of the object.
     */
    public static <T> void delete(final Object object, final OnFailureListener onFailureListener) {
        try {
            checkRegistration(object);
            final String documentID = Reflector.getIDField(object);
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            reference.delete().get();
            Reflector.setIDField(object, null);
        } catch (InterruptedException | ExecutionException | IllegalAccessException | NoSuchFieldException | ClassRegistrationException | NotInitializedException e) {
            onFailureListener.onFailure(e);
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
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
            onFailureListener.onFailure(e);
            return null;
        }
    }

    /**
     * Lists available documents of a given type.
     *
     * @param objectClass The type of the documents to filter.
     * @param limit       The maximum number of objects to return.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns an ArrayList of objects of type objectClass.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static <T> ArrayList<T> list(final Class<T> objectClass, final int limit) throws FirestormException {
        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).limit(limit).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            for (final QueryDocumentSnapshot document : documents) {
                documentList.add(document.toObject(objectClass));
            }
            return documentList;
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
            throw new FirestormException(e);
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
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
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
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static <T> ArrayList<T> listAll(final Class<T> objectClass) throws FirestormException {
        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            for (final QueryDocumentSnapshot document : documents) {
                documentList.add(document.toObject(objectClass));
            }
            return documentList;
        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
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
        return new FirestormFilterable<>(firestore.collection(objectClass.getSimpleName()), objectClass);
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

    /**
     * Retrieves a DocumentReference to an object.
     *
     * @param object The object to get the reference for.
     * @param <T> The type of the object.
     * @return Returns DocumentReference.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static <T> DocumentReference getObjectReference(Object object) throws FirestormException {
        try {
            checkRegistration(object);
            final String documentID = Reflector.getIDField(object);
            return firestore.collection(object.getClass().getSimpleName()).document(documentID);
        } catch (IllegalAccessException | NoSuchFieldException | ClassRegistrationException | NotInitializedException e) {
            throw new FirestormException(e);
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
     * Utility method. Registers a listener in the registeredListeners map.
     * @param object The object being listened to.
     * @param listenerRegistration The listener of the object.
     */
    private static void registerListener(final Object object, final ListenerRegistration listenerRegistration) {
        registeredListeners.put(object, listenerRegistration);
    }

    /**
     * Unregisters a listener from the registeredListeners map for an object provided.
     * @param object The object being listened to.
     */
    private static void unregisterListener(final Object object) {
        final ListenerRegistration listenerRegistration = registeredListeners.get(object);
        listenerRegistration.remove();
        registeredListeners.remove(object);
    }

    /**
     * Utility method. Attaches an event listener which listens for updates to an object.
     *
     * @param eventListener An implementation of a FirestormEventListener.
     * @return Returns a ListenerRegistration.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static ListenerRegistration attachListener(final OnObjectUpdateListener eventListener) throws FirestormException {
        try {
            checkRegistration(eventListener.getObjectToListenFor());
            final String documentID = Reflector.getIDField(eventListener.getObjectToListenFor());
            final ListenerRegistration listenerRegistration = firestore.collection(eventListener.getObjectToListenFor().getClass().getSimpleName()).document(documentID).addSnapshotListener(eventListener);
            registerListener(eventListener.getObjectToListenFor(), listenerRegistration);
            return listenerRegistration;
        } catch (NoSuchFieldException | IllegalAccessException | ClassRegistrationException | NotInitializedException e) {
            throw new FirestormException(e);
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
     * Unregisters a listener from an object.
     * @param object The object being listened to.
     */
    public static void detachListener(Object object) {
        final ListenerRegistration listenerRegistration = registeredListeners.get(object);
        listenerRegistration.remove();
        unregisterListener(listenerRegistration);
    }

    /**
     * Detaches a specified listener from a a reference.
     *
     * @param listenerRegistration The listenerRegistration to detach.
     */
    public static void detachListener(ListenerRegistration listenerRegistration) {
        listenerRegistration.remove();
    }

    /**
     * Checks if a provided object has a registered listener.
     * @param object The object.
     * @return Returns true if the object has a registered listener, false otherwise.
     */
    public static boolean hasListener(Object object) {
        return registeredListeners.get(object) != null;
    }

    /**
     * Retrieves a ListenerRegistration attached to the provided object, or null if no listener is attached.
     * @param object The object.
     * @return Returns a ListenerRegistration.
     */
    public static ListenerRegistration getListener(Object object) {
        return registeredListeners.get(object);
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
