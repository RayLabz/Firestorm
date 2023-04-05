package com.raylabz.firestorm.api;

import com.google.cloud.firestore.EventListener;
import com.google.cloud.firestore.QuerySnapshot;
import com.raylabz.firestorm.firestore.FSFilterable;
import com.raylabz.firestorm.firestore.FSObjectChange;
import com.raylabz.firestorm.async.RealtimeUpdateCallback;

import java.util.List;

public abstract class FilterableListener<T> implements EventListener<QuerySnapshot> {

    /**
     * The listener is listening for changes to this filterable item.
     */
    protected final FSFilterable<T> filterable;

    /**
     * A callback to execute when an update is received.
     */
    protected final RealtimeUpdateCallback<List<FSObjectChange<T>>> callback;

    /**
     * Creates a FilterableListener.
     * @param filterable The filterable that this listener will be attached to.
     */
    protected FilterableListener(FSFilterable<T> filterable, RealtimeUpdateCallback<List<FSObjectChange<T>>> callback) {
        this.filterable = filterable;
        this.callback = callback;
    }

    /**
     * Retrieves the object being listened at by this listener.
     * @return Returns the object being listened at by this listener.
     */
    public FSFilterable<T> getFilterable() {
        return filterable;
    }

}
