package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.raylabz.firestorm.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TransactionFilterable<T> extends FirestormFilterable<T> {

    private final Transaction transaction;

    /**
     * Instantiates a class of TransactionFilterable.
     *
     * @param query  The initial query of the filterable.
     * @param objectClass The type of objects this filterable can interact with.
     */
    public TransactionFilterable(Query query, Class<T> objectClass, Transaction transaction) {
        super(query, objectClass);
        this.transaction = transaction;
    }

    /**
     * Fetches the results of a filterable.
     * @return An ArrayList containing the results of a filter.
     */
    @Override
    public QueryResult<T> fetch() {
        ApiFuture<QuerySnapshot> future = transaction.get(query);
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<T>();
            String lastID = null;
            for (final QueryDocumentSnapshot document : documents) {
                T object = document.toObject(objectClass);
                documentList.add((T) object);
                lastID = document.getId();
            }
            if (documentList.isEmpty()) {
                return new QueryResult<>(documentList, null, null);
            }
            else {
                return new QueryResult<T>(documentList, documents.get(documents.size() - 1), lastID);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new TransactionException(e);
        }
    }

}
