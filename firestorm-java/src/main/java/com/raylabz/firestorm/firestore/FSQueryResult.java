package com.raylabz.firestorm.firestore;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

/**
 * Models a result returned from a query.
 * @param <T> The type of objects in the result.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public class FSQueryResult<T> {

    private final ArrayList<T> items;
    private final QueryDocumentSnapshot snapshot;
    private final String lastDocumentID;

    public FSQueryResult(ArrayList<T> items, QueryDocumentSnapshot snapshot, String lastDocumentID) {
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
