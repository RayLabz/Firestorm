package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.raylabz.firestorm.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class FirestormTransaction implements Transaction.Function<Void> {

    private Transaction transaction;

    public void create(final FirestormObject object) {
        final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document();
        object.setId(reference.getId());
        transaction = transaction.create(reference, object);
    }

    public <T> T get(final Class<T> aClass, final String documentID) {
        final DocumentReference documentReference = Firestorm.firestore.collection(aClass.getSimpleName()).document(documentID);
        try {
            DocumentSnapshot snapshot = transaction.get(documentReference).get();
            return snapshot.toObject(aClass);
        } catch (InterruptedException | ExecutionException e) {
            throw new TransactionException(e);
        }
    }

    public void update(final FirestormObject object) {
        final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document(object.getId());
        transaction = transaction.set(reference, object);
    }

    public void delete(final FirestormObject object) {
        final DocumentReference reference = Firestorm.firestore.collection(object.getClass().getSimpleName()).document(object.getId());
        transaction = transaction.delete(reference);
    }

    public <T> ArrayList<T> list(final Class<T> aClass) {
        ApiFuture<QuerySnapshot> future = Firestorm.firestore.collection(aClass.getSimpleName()).get();
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
                T object = snapshot.toObject(aClass);
                documentList.add(object);
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new TransactionException(e);
        }
    }

    public <T> TransactionFilterable<T> filter(final Class<T> aClass) {
        return new TransactionFilterable<T>(Firestorm.firestore.collection(aClass.getSimpleName()), aClass, transaction);
    }

    @Override
    public Void updateCallback(Transaction transaction) throws Exception {
        this.transaction = transaction;
        execute();
        return null;
    }

    public abstract void execute();

}
