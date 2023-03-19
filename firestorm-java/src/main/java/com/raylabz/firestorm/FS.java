package com.raylabz.firestorm;

import com.google.api.core.*;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.exception.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * com.raylabz.firestorm.Firestorm is an object-oriented data access API for Firestore.
 * <a href="https://raylabz.github.io/Firestorm/">Web guide</a>.
 * This is the main class of the com.raylabz.firestorm.Firestorm API, which allows basic interactions with the Firestore.
 * Must be initialized with a FirebaseApp object using the <i>init()</i> method before interacting with the Firestore.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public final class FS {

    /**
     * Static Firestore object used to make queries on Firestore.
     */
    static Firestore firestore;

    /**
     * Stores a list of listeners registered to objects.
     */
    private static final HashMap<Object, ListenerRegistration> registeredObjectListeners = new HashMap<>();
    private static final HashMap<Class<?>, ListenerRegistration> registeredClassListeners = new HashMap<>();

    /**
     * Initializes com.raylabz.firestorm.Firestorm <b><u>after Firebase has been initialized</u></b> using <i>Firebase.initializeApp()</i>.
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
    private FS() { }

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
     * Retrieves the firestore object being managed by Firestorm.
     * @return Returns a Firestore.
     */
    public static Firestore getFirestore() {
        return firestore;
    }

    /**
     * Creates a Firestore document from an object using an ID.
     *
     * @param object An object containing the data to be written in Firestore.
     * @return Returns the document ID of the created document.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    public static FSFuture<WriteResult> create(final Object object) throws FirestormException {
        try {
            checkRegistration(object);
            String idFieldValue = Reflector.getIDFieldValue(object);
            if (idFieldValue == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
            ApiFuture<WriteResult> apiFuture = reference.create(object);
            return FSFuture.fromAPIFuture(apiFuture);
        } catch (ClassRegistrationException | NoSuchFieldException |
                 IllegalAccessException | NotInitializedException | IDFieldException e) {
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
    public static <T> FSFuture<T> get(final Class<T> objectClass, final String documentID) {
        DocumentReference docRef = firestore.collection(objectClass.getSimpleName()).document(documentID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        ApiFuture<T> objectFuture = ApiFutures.transform(future, input -> {
            try {
                if (input.exists()) {
                    return input.toObject(objectClass);
                }
                else {
                    throw new FirestormException("The document with ID '" + documentID + "' does not exist.");
                }
            } catch (NotInitializedException e) {
                throw new FirestormException(e.getMessage());
            }
        }, Firestorm.getSelectedExecutor());
        return FSFuture.fromAPIFuture(objectFuture);
    }

    public static <T> FSFuture<List<T>> getMany(final Class<T> aClass, List<String> ids) {
        final DocumentReference[] documentReferences = new DocumentReference[ids.size()];
        for (int i = 0; i < documentReferences.length; i++) {
            documentReferences[i] = (firestore.collection(aClass.getSimpleName()).document(ids.get(i)));
        }
        final ApiFuture<List<DocumentSnapshot>> future = firestore.getAll(documentReferences);
        ApiFuture<List<T>> objectListFuture = ApiFutures.transform(future, input -> {
            ArrayList<T> retItems = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : input) {
                if (documentSnapshot.exists()) {
                    retItems.add(documentSnapshot.toObject(aClass));
                }
            }
            return retItems;
        }, Firestorm.getSelectedExecutor());
        return FSFuture.fromAPIFuture(objectListFuture);
    }

    /**
     * Retrieves multiple documents of a class as a list of objects.
     * @param aClass The class of the objects.
     * @param ids An array of IDs of the objects to retrieve.
     * @param <T> A type matching the type of object class.
     * @return Returns a list of type T.
     */
    public static <T> FSFuture<List<T>> getMany(final Class<T> aClass, String... ids) {
        final DocumentReference[] documentReferences = new DocumentReference[ids.length];
        for (int i = 0; i < documentReferences.length; i++) {
            documentReferences[i] = (firestore.collection(aClass.getSimpleName()).document(ids[i]));
        }
        final ApiFuture<List<DocumentSnapshot>> future = firestore.getAll(documentReferences);
        ApiFuture<List<T>> objectListFuture = ApiFutures.transform(future, input -> {
            ArrayList<T> retItems = new ArrayList<>();
            for (DocumentSnapshot documentSnapshot : input) {
                if (documentSnapshot.exists()) {
                    retItems.add(documentSnapshot.toObject(aClass));
                }
            }
            return retItems;
        }, Firestorm.getSelectedExecutor());
        return FSFuture.fromAPIFuture(objectListFuture);
    }

    //TODO ---- CONTINUE TRANSFORMING HERE!

    /**
     * Checks if a given documentID of a given class exists.
     *
     * @param objectClass The object class.
     * @param documentID The document ID.
     * @return Returns true if the document exists on Firestore, false otherwise.
     * @throws FirestormException Thrown when com.raylabz.firestorm.Firestorm encounters an error.
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

//    /**
//     * Updates a document in Firestore.
//     *
//     * @param object An object which provides data and the document ID for the update.
//     * @param onFailureListener FailureListener to execute onFailure().
//     */
//    public static void update(final Object object, final OnFailureListener onFailureListener) {
//        try {
//            checkRegistration(object);
//            final String documentID = Reflector.getIDFieldValue(object);
//            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
//            reference.set(object).get();
//        } catch (InterruptedException | ExecutionException | ClassRegistrationException | IllegalAccessException | NoSuchFieldException | NotInitializedException e) {
//            onFailureListener.onFailure(e);
//        }
//    }
//
//    /**
//     * Updates a document in Firestore.
//     *
//     * @param object An object which provides data and the document ID for the update.
//     * @throws FirestormException Thrown when com.raylabz.firestorm.Firestorm encounters an error.
//     */
//    public static void update(final Object object) throws FirestormException {
//        try {
//            checkRegistration(object);
//            final String documentID = Reflector.getIDFieldValue(object);
//            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
//            reference.set(object).get();
//        } catch (InterruptedException | ExecutionException | ClassRegistrationException | IllegalAccessException | NoSuchFieldException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Deletes an object from Firestore.
//     *
//     * @param objectClass The class of the object to delete.
//     * @param objectID The ID of the object/document in Firestore.
//     * @param <T> The type (class) of the object.
//     * @throws FirestormException Thrown when com.raylabz.firestorm.Firestorm encounters an error.
//     */
//    public static <T> void delete(final Class<T> objectClass, final String objectID) throws FirestormException {
//        final DocumentReference reference = firestore.collection(objectClass.getSimpleName()).document(objectID);
//        try {
//            reference.delete().get();
//        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Deletes an object from Firestore.
//     *
//     * @param objectClass The class of the object to delete.
//     * @param objectID The ID of the object/document in Firestore.
//     * @param <T> The type (class) of the object.
//     * @param onFailureListener OnFailureListener to execute onFailure().
//     * @throws NotInitializedException Thrown when the reference to an object cannot be initialized.
//     */
//    public static <T> void delete(final Class<T> objectClass, final String objectID, final OnFailureListener onFailureListener) {
//        final DocumentReference reference = firestore.collection(objectClass.getSimpleName()).document(objectID);
//        try {
//            reference.delete().get();
//        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
//            onFailureListener.onFailure(e);
//        }
//    }
//
//    /**
//     * Deletes an object from Firestore.
//     *
//     * @param object The object to delete.
//     * @param <T> The type (class) of the object.
//     * @throws FirestormException Thrown when com.raylabz.firestorm.Firestorm encounters an error.
//     */
//    public static <T> void delete(final Object object) throws FirestormException {
//        try {
//            checkRegistration(object);
//            final String documentID = Reflector.getIDFieldValue(object);
//            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
//            reference.delete().get();
//            Reflector.setIDFieldValue(object, null);
//        } catch (InterruptedException | ExecutionException | IllegalAccessException | NoSuchFieldException | ClassRegistrationException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Deletes an object from Firestore.
//     *
//     * @param object The object to delete.
//     * @param onFailureListener OnFailureListener to execute onFailure().
//     * @param <T> The type (class) of the object.
//     */
//    public static <T> void delete(final Object object, final OnFailureListener onFailureListener) {
//        try {
//            checkRegistration(object);
//            final String documentID = Reflector.getIDFieldValue(object);
//            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
//            reference.delete().get();
//            Reflector.setIDFieldValue(object, null);
//        } catch (InterruptedException | ExecutionException | IllegalAccessException | NoSuchFieldException | ClassRegistrationException | NotInitializedException e) {
//            onFailureListener.onFailure(e);
//        }
//    }
//
//    /**
//     * Lists available documents of a given type.
//     *
//     * @param objectClass The type of the documents to filter.
//     * @param limit       The maximum number of objects to return.
//     * @param <T>         A type matching the type of objectClass.
//     * @param onFailureListener OnFailureListener to execute onFailure().
//     * @return Returns an ArrayList of objects of type objectClass.
//     */
//    public static <T> ArrayList<T> list(final Class<T> objectClass, final int limit, final OnFailureListener onFailureListener) {
//        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).limit(limit).get();
//        try {
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            ArrayList<T> documentList = new ArrayList<>();
//            for (final QueryDocumentSnapshot document : documents) {
//                documentList.add(document.toObject(objectClass));
//            }
//            return documentList;
//        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
//            onFailureListener.onFailure(e);
//            return null;
//        }
//    }
//
//    /**
//     * Lists available documents of a given type.
//     *
//     * @param objectClass The type of the documents to filter.
//     * @param limit       The maximum number of objects to return.
//     * @param <T>         A type matching the type of objectClass.
//     * @return Returns an ArrayList of objects of type objectClass.
//     * @throws FirestormException Thrown when com.raylabz.firestorm.Firestorm encounters an error.
//     */
//    public static <T> ArrayList<T> list(final Class<T> objectClass, final int limit) throws FirestormException {
//        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).limit(limit).get();
//        try {
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            ArrayList<T> documentList = new ArrayList<>();
//            for (final QueryDocumentSnapshot document : documents) {
//                documentList.add(document.toObject(objectClass));
//            }
//            return documentList;
//        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Lists ALL available documents of a given type. May incur charges for read operations for huge numbers of documents.
//     *
//     * @param objectClass The type of the documents to filter.
//     * @param <T>         A type matching the type of objectClass.
//     * @param onFailureListener OnFailureListener to execute onFailure().
//     * @return Returns an ArrayList of objects of type objectClass.
//     */
//    public static <T> ArrayList<T> listAll(final Class<T> objectClass, final OnFailureListener onFailureListener) {
//        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).get();
//        try {
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            ArrayList<T> documentList = new ArrayList<>();
//            for (final QueryDocumentSnapshot document : documents) {
//                documentList.add(document.toObject(objectClass));
//            }
//            return documentList;
//        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
//            onFailureListener.onFailure(e);
//            return null;
//        }
//    }
//
//    /**
//     * Lists ALL available documents of a given type. May incur charges for read operations for huge numbers of documents.
//     *
//     * @param objectClass The type of the documents to filter.
//     * @param <T>         A type matching the type of objectClass.
//     * @return Returns an ArrayList of objects of type objectClass.
//     * @throws FirestormException Thrown when com.raylabz.firestorm.Firestorm encounters an error.
//     */
//    public static <T> ArrayList<T> listAll(final Class<T> objectClass) throws FirestormException {
//        ApiFuture<QuerySnapshot> future = firestore.collection(objectClass.getSimpleName()).get();
//        try {
//            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//            ArrayList<T> documentList = new ArrayList<>();
//            for (final QueryDocumentSnapshot document : documents) {
//                documentList.add(document.toObject(objectClass));
//            }
//            return documentList;
//        } catch (InterruptedException | ExecutionException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Lists a set documents which match the filtering criteria provided. Returns a filter of all documents if no filters are used.
//     *
//     * @param objectClass The type of the documents to filter.
//     * @param <T>         A type matching the type of objectClass.
//     * @return Returns a FirestormFilterable which can be used to append filter parameters.
//     */
//    public static <T> FirestormFilterable<T> filter(final Class<T> objectClass) {
//        return new FirestormFilterable<>(firestore.collection(objectClass.getSimpleName()), objectClass);
//    }
//
//    /**
//     * Retrieves a DocumentReference to an object.
//     *
//     * @param objectClass The type of the object.
//     * @param documentID The object's ID.
//     * @param <T> The type of the object.
//     * @return Returns DocumentReference.
//     */
//    public static <T> DocumentReference getObjectReference(final Class<T> objectClass, final String documentID) {
//        return firestore.collection(objectClass.getSimpleName()).document(documentID);
//    }
//
//    /**
//     * Retrieves a DocumentReference to an object.
//     *
//     * @param object The object to get the reference for.
//     * @param <T> The type of the object.
//     * @return Returns DocumentReference.
//     * @throws FirestormException Thrown when com.raylabz.firestorm.Firestorm encounters an error.
//     */
//    public static <T> DocumentReference getObjectReference(Object object) throws FirestormException {
//        try {
//            checkRegistration(object);
//            final String documentID = Reflector.getIDFieldValue(object);
//            return firestore.collection(object.getClass().getSimpleName()).document(documentID);
//        } catch (IllegalAccessException | NoSuchFieldException | ClassRegistrationException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Retrieves a CollectionReference to a type.
//     *
//     * @param objectClass The class of object to get a reference for.
//     * @param <T> The Type of class.
//     * @return Returns a CollectionReference.
//     */
//    public static <T> CollectionReference getCollectionReference(final Class<T> objectClass) {
//        return firestore.collection(objectClass.getSimpleName());
//    }
//
//    /**
//     * Utility method. Registers a listener in the registeredListeners map.
//     * @param object The object being listened to.
//     * @param listenerRegistration The listener of the object.
//     */
//    private static void registerObjectListener(final Object object, final ListenerRegistration listenerRegistration) {
//        registeredObjectListeners.put(object, listenerRegistration);
//    }
//
//    /**
//     * Unregisters a listener for an object provided.
//     * @param object The object being listened to.
//     */
//    private static void unregisterObjectListener(final Object object) {
//        final ListenerRegistration listenerRegistration = registeredObjectListeners.get(object);
//        listenerRegistration.remove();
//        registeredObjectListeners.remove(object);
//    }
//
//    /**
//     * Utility method. Registers a class listener in the registeredListeners map.
//     * @param objectClass The class being listened to.
//     * @param listenerRegistration The listener of the class.
//     */
//    private static void registerClassListener(final Class<?> objectClass, final ListenerRegistration listenerRegistration) {
//        registeredClassListeners.put(objectClass, listenerRegistration);
//    }
//
//    /**
//     * Unregisters a listener for a class provided.
//     * @param objectClass The class being listened to.
//     */
//    private static void unregisterClassListener(final Class<?> objectClass) {
//        final ListenerRegistration listenerRegistration = registeredClassListeners.get(objectClass);
//        listenerRegistration.remove();
//        registeredClassListeners.remove(objectClass);
//    }
//
//    /**
//     * Utility method. Attaches an event listener which listens for updates to an object.
//     *
//     * @param eventListener An implementation of a FirestormEventListener.
//     * @return Returns a ListenerRegistration.
//     * @throws FirestormException Thrown when com.raylabz.firestorm.Firestorm encounters an error.
//     */
//    public static ListenerRegistration attachListener(final ObjectListener eventListener) throws FirestormException {
//        try {
//            checkRegistration(eventListener.getObjectToListenFor());
//            final String documentID = Reflector.getIDFieldValue(eventListener.getObjectToListenFor());
//            final ListenerRegistration listenerRegistration = firestore.collection(eventListener.getObjectToListenFor().getClass().getSimpleName()).document(documentID).addSnapshotListener(eventListener);
//            registerObjectListener(eventListener.getObjectToListenFor(), listenerRegistration);
//            return listenerRegistration;
//        } catch (NoSuchFieldException | IllegalAccessException | ClassRegistrationException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Attaches an event listener which listens for updates to an object using its class and ID.
//     *
//     * @param eventListener An implementation of a FirestormEventListener.
//     * @return Returns a ListenerRegistration.
//     */
//    public static ListenerRegistration attachListener(final ReferenceListener eventListener) {
//        return firestore
//                .collection(eventListener.getObjectClass().getSimpleName())
//                .document(eventListener.getDocumentID())
//                .addSnapshotListener(eventListener);
//    }
//
//    /**
//     * Attaches an event listener which listens for updates to a class.
//     * @param eventListener The collection event listener.
//     * @param <T> The class of the listener.
//     * @return Returns a listener registration.
//     */
//    public static <T> ListenerRegistration attachListener(final ClassListener<T> eventListener) {
//        try {
//            checkRegistration(eventListener.getObjectClass());
//            ListenerRegistration listenerRegistration = firestore.
//                    collection(eventListener.getObjectClass().getSimpleName())
//                    .addSnapshotListener(eventListener);
//            registerClassListener(eventListener.getObjectClass(), listenerRegistration);
//            return listenerRegistration;
//        } catch (ClassRegistrationException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Attaches a listener to a filterable.
//     * @param eventListener The event listener to attach.
//     * @param <T> The type of object.
//     * @return Returns a ListenerRegistration.
//     */
//    public static <T> ListenerRegistration attachListener(final FilterableListener<T> eventListener) {
//        try {
//            checkRegistration(eventListener.getFilterable().objectClass);
//            return eventListener.getFilterable()
//                    .addSnapshotListener(eventListener);
//        } catch (ClassRegistrationException | NotInitializedException e) {
//            throw new FirestormException(e);
//        }
//    }
//
//    /**
//     * Detaches a listener for a specific class.
//     * @param objectClass The class.
//     */
//    public static void detachListener(Class<?> objectClass) {
//        final ListenerRegistration listenerRegistration = registeredClassListeners.get(objectClass);
//        listenerRegistration.remove();
//        unregisterClassListener(objectClass);
//    }
//
//    /**
//     * Unregisters a listener from an object.
//     * @param object The object being listened to.
//     */
//    public static void detachListener(Object object) {
//        final ListenerRegistration listenerRegistration = registeredObjectListeners.get(object);
//        listenerRegistration.remove();
//        unregisterObjectListener(listenerRegistration);
//    }
//
//    /**
//     * Detaches a specified listener from a reference.
//     *
//     * @param listenerRegistration The listenerRegistration to detach.
//     */
//    public static void detachListener(ListenerRegistration listenerRegistration) {
//        listenerRegistration.remove();
//    }
//
//    /**
//     * Checks if a provided object has a registered listener.
//     * @param object The object.
//     * @return Returns true if the object has a registered listener, false otherwise.
//     */
//    public static boolean hasListener(Object object) {
//        return registeredObjectListeners.get(object) != null;
//    }
//
//    /**
//     * Checks if a provided class has a registered listener.
//     * @param objectClass The class.
//     * @param <T> The type of class.
//     * @return Returns true if the object has a registered listener, false otherwise.
//     */
//    public static <T> boolean hasListener(Class<T> objectClass) {
//        return registeredClassListeners.get(objectClass) != null;
//    }
//
//    /**
//     * Retrieves a ListenerRegistration attached to the provided object, or null if no listener is attached.
//     * @param object The object.
//     * @return Returns a ListenerRegistration.
//     */
//    public static ListenerRegistration getListener(Object object) {
//        return registeredObjectListeners.get(object);
//    }
//
//    /**
//     * Runs a transaction operation.
//     *
//     * @param transaction The transaction to run.
//     */
//    public static void runTransaction(final FirestormTransaction transaction) {
//        ApiFuture<Void> futureTransaction = FS.firestore.runTransaction(transaction);
//        try {
//            futureTransaction.get();
//            transaction.onSuccess();
//        } catch (InterruptedException | ExecutionException e) {
//            transaction.onFailure(e);
//        } catch (NullPointerException e) {
//            throw new NotInitializedException();
//        }
//    }
//
//    /**
//     * Runs a batch write operation.
//     *
//     * @param batch The batch to run.
//     */
//    public static void runBatch(final FirestormBatch batch) {
//        batch.doBatch();
//    }

}
