package com.raylabz.firestorm.firestore;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.Transaction;
import com.raylabz.firestorm.Firestorm;
import com.raylabz.firestorm.async.FSFuture;

import java.util.ArrayList;
import java.util.List;

/**
 * A filterable used in transactions.
 * @param <T> The type of objects used in this transaction filterable.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class FSTransactionFilterable<T> extends FSFilterable<T> {

    private final Transaction transaction;

    /**
     * Instantiates a class of TransactionFilterable.
     *
     * @param query  The initial query of the filterable.
     * @param objectClass The type of objects this filterable can interact with.
     * @param transaction The transaction object passed from Firestore.
     */
    public FSTransactionFilterable(Query query, Class<T> objectClass, Transaction transaction) {
        super(query, objectClass);
        this.transaction = transaction;
    }

    /**
     * Fetches the results of a filterable.
     * @return An ArrayList containing the results of a filter.
     */
    @Override
    public FSFuture<FSQueryResult<T>> fetch() {
        ApiFuture<QuerySnapshot> future = query.get();
        ApiFuture<FSQueryResult<T>> queryFuture = ApiFutures.transform(future, input -> {
            List<QueryDocumentSnapshot> documents = input.getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            String lastID = null;
            for (final QueryDocumentSnapshot document : documents) {
                T object = document.toObject(objectClass);
                documentList.add(object);
                lastID = document.getId();
            }
            if (documentList.isEmpty()) {
                return new FSQueryResult<>(documentList, null, null);
            }
            else {
                return new FSQueryResult<>(documentList, documents.get(documents.size() - 1), lastID);
            }
        }, Firestorm.getSelectedExecutor());
        return FSFuture.fromAPIFuture(queryFuture);
    }

}
