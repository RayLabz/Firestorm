package com.raylabz.firestorm.rdb;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.raylabz.firestorm.api.Filterable;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.rdb.callables.FilterableCallable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * An object that enables quick use of RDB filters.
 * @author Nicos Kasenides
 * @version 2.0.0
 * @param <T> The type of objects filtered.
 */
public class OrderedRDBFilterable<T> extends Filterable<Query, T> {

    /**
     * Constructs a new filterable object
     *
     * @param query       The query object used for filtering.
     * @param objectClass The object class.
     */
    private OrderedRDBFilterable(Query query, Class<T> objectClass) {
        super(query, objectClass);
    }

    /**
     * Utility method that helps create an ordered filterable.
     * @param filterable The normal filterable.
     * @return Returns an {@link OrderedRDBFilterable}.
     * @param <T> The type used by the filterable.
     */
    public static <T> OrderedRDBFilterable<T> fromFilterable(RDBFilterable<T> filterable) {
        return new OrderedRDBFilterable<>(filterable.getQuery(), filterable.getObjectClass());
    }

    /**
     * Limits the query results to the first <b>limit</b> entries.
     * @param limit The limit.
     * @return Returns a query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> limitToFirst(int limit) {
        query = query.limitToFirst(limit);
        return this;
    }

    /**
     * Limits the query results to the last <b>limit</b> entries.
     * @param limit The limit.
     * @return Returns a query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> limitToLast(int limit) {
        query = query.limitToLast(limit);
        return this;
    }

    /**
     * Adds a listener to the object.
     * @param listener The listener.
     * @return Returns a query.
     */
    public ValueEventListener addValueEventListener(ValueEventListener listener) {
        return query.addValueEventListener(listener);
    }

    /**
     * Adds a child listener to the object.
     * @param listener The listener.
     * @return Returns a query.
     */
    public ChildEventListener addChildEventListener(ChildEventListener listener) {
        return query.addChildEventListener(listener);
    }

    /**
     * Fetches the results of the filterable.
     * @return Returns an {@link FSFuture}.
     */
    public FSFuture<List<T>> fetch() {
        FilterableCallable<T> filterableCallable = new FilterableCallable<>(objectClass, query);
        return FSFuture.fromCallable(filterableCallable);
    }

}
