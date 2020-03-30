package com.raylabz.firestorm;

import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class QueryResult<T> {

    private final ArrayList<T> results;
    private final QueryDocumentSnapshot snapshot;
    private final String lastDocumentID;

    public QueryResult(ArrayList<T> results, QueryDocumentSnapshot snapshot, String lastDocumentID) {
        this.results = results;
        this.snapshot = snapshot;
        this.lastDocumentID = lastDocumentID;
    }

    public ArrayList<T> getResults() {
        return results;
    }

    public QueryDocumentSnapshot getSnapshot() {
        return snapshot;
    }

    public String getLastDocumentID() {
        return lastDocumentID;
    }

}
