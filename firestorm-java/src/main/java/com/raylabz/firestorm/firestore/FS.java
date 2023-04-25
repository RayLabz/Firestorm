package com.raylabz.firestorm.firestore;

import com.google.api.core.*;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.raylabz.firestorm.*;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;
import com.raylabz.firestorm.exception.*;
import com.raylabz.firestorm.util.Reflector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * FS (Firestore) provides an API that enables operations to be carried out for Firestore.
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
     * Initializes the Firestore API for Firestorm <b><u>after Firebase has been initialized</u></b> using <i>Firebase.initializeApp()</i>.
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
     * Closes the connection to Firestore.
     * @throws Exception Occurs when the connection cannot be closed.
     */
    public static void close() throws Exception {
        firestore.close();
    }

    /**
     * Private constructor.
     */
    private FS() { }

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
            Firestorm.checkRegistration(object);
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
     * Creates multiple objects.
     *
     * @param objects The objects to create.
     * @return Returns a list of {@link WriteResult}.
     * @param <T> The type of objects to create.
     */
    public static <T> FSFuture<List<WriteResult>> create(List<T> objects) {
        try {
            if (!objects.isEmpty()) {
                Firestorm.checkRegistration(objects.get(0).getClass());
                ArrayList<DocumentReference> documentReferences = new ArrayList<>();
                //Find IDs:
                for (T object : objects) {
                    String idFieldValue = Reflector.getIDFieldValue(object);
                    if (idFieldValue == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
                    documentReferences.add(reference);
                }
                if (documentReferences.size() != objects.size()) {
                    throw new FirestormException("Objects list size must be the same as the DocumentReference list size.");
                }

                WriteBatch batch = firestore.batch();
                for (int i = 0; i < documentReferences.size(); i++) {
                    batch.set(documentReferences.get(i), objects.get(i));
                }
                return FSFuture.fromAPIFuture(batch.commit());
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Creates multiple objects.
     *
     * @param objects The objects to create.
     * @return Returns a list of {@link WriteResult}.
     * @param <T> The type of objects to create.
     */
    @SafeVarargs
    public static <T> FSFuture<List<WriteResult>> create(T... objects) {
        try {
            if (objects.length != 0) {
                Firestorm.checkRegistration(objects[0].getClass());
                ArrayList<DocumentReference> documentReferences = new ArrayList<>();
                //Find IDs:
                for (T object : objects) {
                    String idFieldValue = Reflector.getIDFieldValue(object);
                    if (idFieldValue == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
                    documentReferences.add(reference);
                }
                if (documentReferences.size() != objects.length) {
                    throw new FirestormException("Objects list size must be the same as the DocumentReference list size.");
                }

                WriteBatch batch = firestore.batch();
                for (int i = 0; i < documentReferences.size(); i++) {
                    batch.set(documentReferences.get(i), objects[i]);
                }
                return FSFuture.fromAPIFuture(batch.commit());
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
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

    /**
     * Retrieves multiple documents based on a list of IDs.
     * @param aClass The class of documents.
     * @param ids The IDs list.
     * @return Returns a future of List.
     * @param <T> The type of object.
     */
    public static <T> FSFuture<List<T>> get(final Class<T> aClass, List<String> ids) {
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
    public static <T> FSFuture<List<T>> get(final Class<T> aClass, String... ids) {
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

    /**
     * Checks if a given documentID of a given class exists.
     *
     * @param aClass The object class.
     * @param id The document ID.
     * @return Returns true if the document exists on Firestore, false otherwise.
     */
    public static <T> FSFuture<Boolean> exists(final Class<T> aClass, String id) {
        DocumentReference docRef = firestore.collection(aClass.getSimpleName()).document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        ApiFuture<Boolean> existsFuture = ApiFutures.transform(
                future,
                DocumentSnapshot::exists,
                Firestorm.getSelectedExecutor()
        );
        return FSFuture.fromAPIFuture(existsFuture);
    }

    /**
     * Checks if a given object exists.
     *
     * @param object The object to check.
     * @param <T> The type of object.
     * @return Returns true if the object exists on Firestore, false otherwise.
     */
    public static <T> FSFuture<Boolean> exists(final T object) throws FirestormException {
        try {
            String id = Reflector.getIDFieldValue(object);
            if (id == null) {
                throw new FirestormException("ID field cannot be null");
            }
            DocumentReference docRef = firestore.collection(object.getClass().getSimpleName()).document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            ApiFuture<Boolean> existsFuture = ApiFutures.transform(
                    future,
                    DocumentSnapshot::exists,
                    Firestorm.getSelectedExecutor()
            );
            return FSFuture.fromAPIFuture(existsFuture);
        } catch (IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Updates a document in Firestore, replacing any existing objects with the same ID.
     * If an object with this ID does not exist, it will be created.
     *
     * @param object The object to update.
     * @return Returns a {@link WriteResult}.
     */
    public static FSFuture<WriteResult> update(final Object object) throws FirestormException {
        try {
            Firestorm.checkRegistration(object);
            String idFieldValue = Reflector.getIDFieldValue(object);
            if (idFieldValue == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
            ApiFuture<WriteResult> apiFuture = reference.set(object);
            return FSFuture.fromAPIFuture(apiFuture);
        } catch (ClassRegistrationException | NoSuchFieldException |
                 IllegalAccessException | NotInitializedException | IDFieldException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Updates multiple objects.
     *
     * @param objects The objects to update.
     * @return Returns a list of {@link WriteResult}.
     * @param <T> The type of objects to update.
     */
    public static <T> FSFuture<List<WriteResult>> update(List<T> objects) throws FirestormException {
        try {
            if (!objects.isEmpty()) {
                Firestorm.checkRegistration(objects.get(0).getClass());
                ArrayList<DocumentReference> documentReferences = new ArrayList<>();
                //Find IDs:
                for (T object : objects) {
                    String idFieldValue = Reflector.getIDFieldValue(object);
                    if (idFieldValue == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
                    documentReferences.add(reference);
                }
                if (documentReferences.size() != objects.size()) {
                    throw new FirestormException("Objects list size must be the same as the DocumentReference list size.");
                }

                WriteBatch batch = firestore.batch();
                for (int i = 0; i < documentReferences.size(); i++) {
                    batch.set(documentReferences.get(i), objects.get(i));
                }
                return FSFuture.fromAPIFuture(batch.commit());
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Updates multiple objects.
     *
     * @param objects The objects to update.
     * @return Returns a list of {@link WriteResult}.
     * @param <T> The type of objects to update.
     */
    @SafeVarargs
    public static <T> FSFuture<List<WriteResult>> update(T... objects) throws FirestormException {
        try {
            if (objects.length != 0) {
                Firestorm.checkRegistration(objects[0].getClass());
                ArrayList<DocumentReference> documentReferences = new ArrayList<>();
                //Find IDs:
                for (T object : objects) {
                    String idFieldValue = Reflector.getIDFieldValue(object);
                    if (idFieldValue == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
                    documentReferences.add(reference);
                }
                if (documentReferences.size() != objects.length) {
                    throw new FirestormException("Objects list size must be the same as the DocumentReference list size.");
                }

                WriteBatch batch = firestore.batch();
                for (int i = 0; i < documentReferences.size(); i++) {
                    batch.set(documentReferences.get(i), objects[i]);
                }
                return FSFuture.fromAPIFuture(batch.commit());
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes an object from Firestore.
     *
     * @param object  The object to delete.
     * @return Returns a {@link WriteResult}.
     */
    public static FSFuture<WriteResult> delete(Object object) throws FirestormException {
        try {
            final String documentID = Reflector.getIDFieldValue(object);
            if (documentID == null) {
                throw new FirestormException("ID field cannot be null");
            }
            final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(documentID);
            FSFuture<WriteResult> writeResultFSFuture = FSFuture.fromAPIFuture(reference.delete());
            Reflector.setIDFieldValue(object, null);
            return writeResultFSFuture;
        } catch (IllegalAccessException | NoSuchFieldException | NotInitializedException | IDFieldException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes an object from Firestore using its class and ID.
     *
     * @param aClass The object's class.
     * @param id The object's ID.
     * @param <T> The object's type.
     * @return Returns a {@link WriteResult}.
     */
    public static <T> FSFuture<WriteResult> delete(final Class<T> aClass, final String id) {
        if (id == null) {
            throw new FirestormException("ID field cannot be null");
        }
        final DocumentReference reference = firestore.collection(aClass.getSimpleName()).document(id);
        return FSFuture.fromAPIFuture(reference.delete());
    }

    /**
     * Deletes multiple objects.
     *
     * @param objects The objects to delete.
     * @return Returns a list of {@link WriteResult}.
     * @param <T> The type of objects to create.
     */
    public static <T> FSFuture<List<WriteResult>> delete(List<T> objects) {
        try {
            if (!objects.isEmpty()) {
                Firestorm.checkRegistration(objects.get(0).getClass());
                ArrayList<DocumentReference> documentReferences = new ArrayList<>();
                //Find IDs:
                for (T object : objects) {
                    String idFieldValue = Reflector.getIDFieldValue(object);
                    if (idFieldValue == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
                    documentReferences.add(reference);
                }
                if (documentReferences.size() != objects.size()) {
                    throw new FirestormException("Objects list size must be the same as the DocumentReference list size.");
                }

                WriteBatch batch = firestore.batch();
                for (DocumentReference documentReference : documentReferences) {
                    batch.delete(documentReference);
                }
                for (T object : objects) {
                    Reflector.setIDFieldValue(object, null);
                }
                return FSFuture.fromAPIFuture(batch.commit());
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes multiple objects.
     *
     * @param objects The objects to delete.
     * @return Returns a list of {@link WriteResult}.
     * @param <T> The type of objects to delete.
     */
    @SafeVarargs
    public static <T> FSFuture<List<WriteResult>> delete(T... objects) {
        try {
            if (objects.length != 0) {
                Firestorm.checkRegistration(objects[0].getClass());
                ArrayList<DocumentReference> documentReferences = new ArrayList<>();
                //Find IDs:
                for (T object : objects) {
                    String idFieldValue = Reflector.getIDFieldValue(object);
                    if (idFieldValue == null) {
                        throw new FirestormException("ID field cannot be null");
                    }
                    final DocumentReference reference = firestore.collection(object.getClass().getSimpleName()).document(idFieldValue);
                    documentReferences.add(reference);
                }
                if (documentReferences.size() != objects.length) {
                    throw new FirestormException("Objects list size must be the same as the DocumentReference list size.");
                }

                WriteBatch batch = firestore.batch();
                for (DocumentReference documentReference : documentReferences) {
                    batch.delete(documentReference);
                }
                for (T object : objects) {
                    Reflector.setIDFieldValue(object, null);
                }
                return FSFuture.fromAPIFuture(batch.commit());
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException | IDFieldException | NoSuchFieldException | IllegalAccessException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes multiple objects.
     *
     * @param aClass The object class.
     * @param ids The IDs of the objects to delete.
     * @return Returns a list of {@link WriteResult}.
     * @param <T> The type of objects to delete.
     */
    public static <T> FSFuture<List<WriteResult>> delete(final Class<T> aClass, String... ids) {
        try {
            if (ids.length != 0) {
                Firestorm.checkRegistration(aClass);
                ArrayList<DocumentReference> documentReferences = new ArrayList<>();
                for (String id : ids) {
                    documentReferences.add(firestore.collection(aClass.getSimpleName()).document(id));
                }

                WriteBatch batch = firestore.batch();
                for (DocumentReference documentReference : documentReferences) {
                    batch.delete(documentReference);
                }
                return FSFuture.fromAPIFuture(batch.commit());
            }
            throw new FirestormException("Objects array cannot be empty.");
        } catch (ClassRegistrationException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Deletes ALL documents/objects of a certain type (WARNING: Destructive action).
     *
     * @param aClass The class/type.
     * @return Returns a list of {@link WriteResult}.
     * @param <T> The type.
     */
    public static <T> FSFuture<List<WriteResult>> deleteType(final Class<T> aClass) {
        try {
            Firestorm.checkRegistration(aClass);
            Iterable<DocumentReference> documents = firestore.collection(aClass.getSimpleName()).listDocuments();
            WriteBatch batch = firestore.batch();
            for (DocumentReference documentReference : documents) {
                batch.delete(documentReference);
            }
            return FSFuture.fromAPIFuture(batch.commit());
        } catch (ClassRegistrationException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Lists all objects/documents of a specified type up to a certain limit.
     *
     * @param aClass The class of the object.
     * @param limit The limit (return up to a certain number of items).
     * @return Returns list of {@link T}.
     * @param <T> The type of the object.
     */
    public static <T> FSFuture<List<T>> list(final Class<T> aClass, int limit) {
        ApiFuture<QuerySnapshot> future = firestore.collection(aClass.getSimpleName()).limit(limit).get();
        ApiFuture<List<T>> transformFuture = ApiFutures.transform(future, input -> {
            ArrayList<T> retItems = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = input.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                retItems.add(document.toObject(aClass));
            }
            return retItems;
        }, Firestorm.getSelectedExecutor());
        return FSFuture.fromAPIFuture(transformFuture);
    }

    /**
     * Lists all objects/documents of a specified type.
     *
     * @param aClass The class of the object.
     * @return Returns list of {@link T}.
     * @param <T> The type of the object.
     */
    public static <T> FSFuture<List<T>> list(final Class<T> aClass) {
        ApiFuture<QuerySnapshot> future = firestore.collection(aClass.getSimpleName()).get();
        ApiFuture<List<T>> transformFuture = ApiFutures.transform(future, input -> {
            ArrayList<T> retItems = new ArrayList<>();
            List<QueryDocumentSnapshot> documents = input.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                retItems.add(document.toObject(aClass));
            }
            return retItems;
        }, Firestorm.getSelectedExecutor());
        return FSFuture.fromAPIFuture(transformFuture);
    }

    /**
     * Lists a set documents which match the filtering criteria provided. Returns a filter of all documents if no filters are used.
     *
     * @param objectClass The type of the documents to filter.
     * @param <T>         A type matching the type of objectClass.
     * @return Returns a FirestormFilterable which can be used to append filter parameters.
     */
    public static <T> FSFilterable<T> filter(final Class<T> objectClass) {
        return new FSFilterable<>(firestore.collection(objectClass.getSimpleName()), objectClass);
    }

    /**
     * Retrieves a DocumentReference to an object.
     *
     * @param objectClass The type of the object.
     * @param documentID The object's ID.
     * @param <T> The type of the object.
     * @return Returns DocumentReference.
     */
    @Nonnull
    public static <T> DocumentReference getObjectReference(final Class<T> objectClass, final String documentID) {
        return firestore.collection(objectClass.getSimpleName()).document(documentID);
    }

    /**
     * Retrieves a DocumentReference to an object.
     *
     * @param object The object to get the reference for.
     * @return Returns DocumentReference.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    @Nonnull
    public static DocumentReference getObjectReference(Object object) throws FirestormException {
        try {
            Firestorm.checkRegistration(object);
            final String documentID = Reflector.getIDFieldValue(object);
            return firestore.collection(object.getClass().getSimpleName()).document(documentID);
        } catch (IllegalAccessException | NoSuchFieldException | ClassRegistrationException | NotInitializedException |
                 IDFieldException e) {
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
    private static void registerObjectListener(final Object object, final ListenerRegistration listenerRegistration) {
        registeredObjectListeners.put(object, listenerRegistration);
    }

    /**
     * Unregisters a listener for an object provided.
     * @param object The object being listened to.
     */
    private static void unregisterObjectListener(final Object object) {
        final ListenerRegistration listenerRegistration = registeredObjectListeners.get(object);
        listenerRegistration.remove();
        registeredObjectListeners.remove(object);
    }

    /**
     * Utility method. Registers a class listener in the registeredListeners map.
     * @param objectClass The class being listened to.
     * @param listenerRegistration The listener of the class.
     */
    private static void registerClassListener(final Class<?> objectClass, final ListenerRegistration listenerRegistration) {
        registeredClassListeners.put(objectClass, listenerRegistration);
    }

    /**
     * Unregisters a listener for a class provided.
     * @param objectClass The class being listened to.
     */
    private static void unregisterClassListener(final Class<?> objectClass) {
        final ListenerRegistration listenerRegistration = registeredClassListeners.get(objectClass);
        listenerRegistration.remove();
        registeredClassListeners.remove(objectClass);
    }

    /**
     * Utility method. Attaches an event listener which listens for updates to an object.
     *
     * @param objectToListenFor The object to listen to changes.
     * @param callback A callback function allowing the execution of code after a change occurs.
     * @param <T> The type of object to listen to.
     * @return Returns a ListenerRegistration.
     * @throws FirestormException Thrown when Firestorm encounters an error.
     */
    @Nonnull
    public static <T> ListenerRegistration attachListener(final T objectToListenFor, final RealtimeUpdateCallback<T> callback) throws FirestormException {
        try {
            Firestorm.checkRegistration(objectToListenFor);
            FSObjectListener<T> listener = new FSObjectListener<>(objectToListenFor, callback);
            final String documentID = Reflector.getIDFieldValue(objectToListenFor);
            final ListenerRegistration listenerRegistration = firestore.collection(objectToListenFor.getClass().getSimpleName()).document(documentID).addSnapshotListener(listener);
            registerObjectListener(objectToListenFor, listenerRegistration);
            return listenerRegistration;
        } catch (NoSuchFieldException | IllegalAccessException | ClassRegistrationException | NotInitializedException |
                 IDFieldException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Attaches an event listener which listens for updates to an object using its class and ID.
     *
     * @param <T> The type of object to listen for.
     * @param objectClass The class of the object.
     * @param documentID The ID of the object.
     * @param callback A callback function to execute code once an update is received.
     * @return Returns a ListenerRegistration.
     */
    public static <T> ListenerRegistration attachListener(final Class<T> objectClass, final String documentID, final RealtimeUpdateCallback<T> callback) {
        FSReferenceListener<T> referenceListener = new FSReferenceListener<>(objectClass, documentID, callback);
        return firestore
                .collection(referenceListener.getObjectClass().getSimpleName())
                .document(referenceListener.getDocumentID())
                .addSnapshotListener(referenceListener);
    }

    /**
     * Attaches an event listener which listens for updates to a class.
     * @param objectClass The class of documents to listen to.
     * @param callback A function to execute when an update is received.
     * @param <T> The class of the listener.
     * @return Returns a listener registration.
     */
    public static <T> ListenerRegistration attachClassListener(Class<T> objectClass, final RealtimeUpdateCallback<List<FSObjectChange<T>>> callback) {
        try {
            Firestorm.checkRegistration(objectClass);
            final FSClassListener<T> classListener = new FSClassListener<>(objectClass, callback);
            ListenerRegistration listenerRegistration = firestore.
                    collection(objectClass.getSimpleName())
                    .addSnapshotListener(classListener);
            registerClassListener(classListener.getObjectClass(), listenerRegistration);
            return listenerRegistration;
        } catch (ClassRegistrationException | NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Attaches a listener to a filterable.
     * @param filterable The filterable to listen to.
     * @param callback A function to execute when an update is received.
     * @param <T> The type of object.
     * @return Returns a ListenerRegistration.
     */
    public static <T> ListenerRegistration attachFilterableListener(FSFilterable<T> filterable, RealtimeUpdateCallback<List<FSObjectChange<T>>> callback) {
        try {
            Firestorm.checkRegistration(filterable.objectClass);
            final FSFilterableListener<T> eventListener = new FSFilterableListener<>(filterable, callback);
            return eventListener.getFilterable()
                    .addSnapshotListener(eventListener);
        } catch (ClassRegistrationException | NotInitializedException e) {
            throw new FirestormException(e);
        }
    }

    /**
     * Detaches a listener for a specific class.
     * @param objectClass The class.
     */
    public static void detachListener(Class<?> objectClass) {
        final ListenerRegistration listenerRegistration = registeredClassListeners.get(objectClass);
        listenerRegistration.remove();
        unregisterClassListener(objectClass);
    }

    /**
     * Unregisters a listener from an object.
     * @param object The object being listened to.
     */
    public static void detachListener(Object object) {
        final ListenerRegistration listenerRegistration = registeredObjectListeners.get(object);
        listenerRegistration.remove();
        unregisterObjectListener(listenerRegistration);
    }

    /**
     * Detaches a specified listener from a reference.
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
        return registeredObjectListeners.get(object) != null;
    }

    /**
     * Checks if a provided class has a registered listener.
     * @param objectClass The class.
     * @param <T> The type of class.
     * @return Returns true if the object has a registered listener, false otherwise.
     */
    public static <T> boolean hasListener(Class<T> objectClass) {
        return registeredClassListeners.get(objectClass) != null;
    }

    /**
     * Retrieves a ListenerRegistration attached to the provided object, or null if no listener is attached.
     * @param object The object.
     * @return Returns a ListenerRegistration.
     */
    public static ListenerRegistration getListener(Object object) {
        return registeredObjectListeners.get(object);
    }

    /**
     * Runs a transaction operation.
     *
     * @param transaction The transaction to run.
     */
    public static FSFuture<Void> runTransaction(final FSTransaction transaction) {
        ApiFuture<Void> futureTransaction = FS.firestore.runTransaction(transaction);
        return FSFuture.fromAPIFuture(futureTransaction);
    }


    /**
     * Runs a batch write operation.
     *
     * @param batch The batch to run.
     * @return Returns a list of {@link WriteResult}.
     */
    public static FSFuture<List<WriteResult>> runBatch(final FSBatch batch) {
        return batch.commit();
    }

}
