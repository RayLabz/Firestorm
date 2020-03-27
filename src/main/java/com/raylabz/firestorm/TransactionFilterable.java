package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TransactionFilterable<T> extends FirestormFilterable<T> {

    private final Transaction transaction;

    /**
     * Instantiates a class of TransactionFilterable.
     *
     * @param query  The initial query of the filterable.
     * @param aClass The type of objects this filterable can interact with.
     */
    public TransactionFilterable(Query query, Class<T> aClass, Transaction transaction) {
        super(query, aClass);
        this.transaction = transaction;
    }

    /**
     * Fetches the results of a filterable.
     * @return An ArrayList containing the results of a filter.
     */
    @Override
    public ArrayList<T> fetch() {
        ApiFuture<QuerySnapshot> future = transaction.get(query);
        try {
            List<QueryDocumentSnapshot> documents = null;
            documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<T>();
            for (final QueryDocumentSnapshot document : documents) {
                Object object = document.toObject(aClass);
                documentList.add((T) object);
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
