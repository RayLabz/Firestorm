package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.exception.TransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A filterable used in transactions.
 * @param <T> The type of objects used in this transaction filterable.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class TransactionFilterable<T> extends FirestormFilterable<T> {

    private final Transaction transaction;

    /**
     * Instantiates a class of TransactionFilterable.
     *
     * @param query  The initial query of the filterable.
     * @param objectClass The type of objects this filterable can interact with.
     * @param transaction The transaction object passed from Firestore.
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
    public FSFuture<QueryResult<T>> fetch() {
        ApiFuture<QuerySnapshot> future = query.get();
        ApiFuture<QueryResult<T>> queryFuture = ApiFutures.transform(future, input -> {
            List<QueryDocumentSnapshot> documents = input.getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            String lastID = null;
            for (final QueryDocumentSnapshot document : documents) {
                T object = document.toObject(objectClass);
                documentList.add(object);
                lastID = document.getId();
            }
            if (documentList.isEmpty()) {
                return new QueryResult<>(documentList, null, null);
            }
            else {
                return new QueryResult<>(documentList, documents.get(documents.size() - 1), lastID);
            }
        }, Firestorm.getSelectedExecutor());
        return FSFuture.fromAPIFuture(queryFuture);
    }

}
