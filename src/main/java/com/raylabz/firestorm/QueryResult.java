package com.raylabz.firestorm;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

/**
 * Models a result returned from a query.
 * @param <T> The type of objects in the result.
 */
public class QueryResult<T> {

    private final ArrayList<T> items;
    private final QueryDocumentSnapshot snapshot;
    private final String lastDocumentID;

    public QueryResult(ArrayList<T> items, QueryDocumentSnapshot snapshot, String lastDocumentID) {
        this.items = items;
        this.snapshot = snapshot;
        this.lastDocumentID = lastDocumentID;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public QueryDocumentSnapshot getSnapshot() {
        return snapshot;
    }

    public String getLastDocumentID() {
        return lastDocumentID;
    }

    public boolean hasItems() {
        return (!items.isEmpty());
    }

}
