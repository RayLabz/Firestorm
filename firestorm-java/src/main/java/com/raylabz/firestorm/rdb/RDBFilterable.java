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
public class RDBFilterable<T> extends Filterable<Query, T> {

    private boolean ordered = false;

    /**
     * Constructs a new filterable object
     *
     * @param query       The query object used for filtering.
     * @param objectClass The object class.
     */
    public RDBFilterable(Query query, Class<T> objectClass) {
        super(query, objectClass);
    }

    /**
     * Limits the query results to the first <b>limit</b> entries.
     * @param limit The limit.
     * @return Returns a query.
     */
    @Nonnull
    public RDBFilterable<T> limitToFirst(int limit) {
        query = query.limitToFirst(limit);
        return this;
    }

    /**
     * Limits the query results to the last <b>limit</b> entries.
     * @param limit The limit.
     * @return Returns a query.
     */
    @Nonnull
    public RDBFilterable<T> limitToLast(int limit) {
        query = query.limitToLast(limit);
        return this;
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public OrderedRDBFilterable<T> startAt(String key, double value) {
        query = query.startAt(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public OrderedRDBFilterable<T> startAt(String key, String value) {
        query = query.startAt(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public OrderedRDBFilterable<T> startAt(String key, boolean value) {
        query = query.startAt(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public OrderedRDBFilterable<T> endAt(String key, double value) {
        query = query.endAt(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public OrderedRDBFilterable<T> endAt(String key, String value) {
        query = query.endAt(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public OrderedRDBFilterable<T> endAt(String key, boolean value) {
        query = query.endAt(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereEqualTo(@Nonnull String key, double value) {
        query = query.equalTo(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereEqualTo(@Nonnull String key, @Nullable String value) {
        query = query.equalTo(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereEqualTo(@Nonnull String key, boolean value) {
        query = query.equalTo(value, key);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereLessThan(@Nonnull String key, double value) {
        orderBy(key);
        query = query.endAt(value - 1);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereLessThan(@Nonnull String key, String value) {
        //Replace the last character with one character previous to that:
        char c = value.charAt(value.length() - 1);
        //Char is > 0, ok to change it to previous one:
        if (c > 0) {
            c--;
            value = value.substring(0, value.length() - 1) + c;
        }
        //Char is 0, remove it completely:
        else {
            value = value.substring(0, value.length() - 1);
        }
        orderBy(key);
        query = query.endAt(value);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereLessThanOrEqualTo(@Nonnull String key, double value) {
        orderBy(key);
        query = query.endAt(value);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereLessThanOrEqualTo(@Nonnull String key, @Nonnull String value) {
        orderBy(key);
        query = query.endAt(value);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereLessThanOrEqualTo(@Nonnull String key, boolean value) {
        orderBy(key);
        query = query.endAt(value);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Filters by value (greater than).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereGreaterThan(@Nonnull String key, double value) {
        orderBy(key);
        query = query.startAt(value + 1);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Filters by value (greater than).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereGreaterThan(@Nonnull String key, String value) {
        char c = 0;
        orderBy(key);
        query = query.startAt(value + c);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Filters by value (greater than or equal to).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String key, double value) {
        orderBy(key);
        query = query.startAt(value);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Filters by value (greater than or equal to).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String key, String value) {
        orderBy(key);
        query = query.startAt(value);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Filters by value (greater than or equal to).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public OrderedRDBFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String key, boolean value) {
        orderBy(key);
        query = query.startAt(value);
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Orders the results by child value.
     * @param key The key of the child (attribute name).
     * @return Returns a query.
     */
    public OrderedRDBFilterable<T> orderBy(String key) {
        query = query.orderByChild(key);
        ordered = true;
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Orders the results by key.
     * @return Returns a query.
     */
    public OrderedRDBFilterable<T> orderByKey() {
        query = query.orderByKey();
        ordered = true;
        return OrderedRDBFilterable.fromFilterable(this);
    }

    /**
     * Orders the results by priority.
     * @return Returns a query.
     */
    public OrderedRDBFilterable<T> orderByPriority() {
        query = query.orderByPriority();
        ordered = true;
        return OrderedRDBFilterable.fromFilterable(this);
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
        if (!ordered) {
            query = query.orderByKey();
        }
        FilterableCallable<T> filterableCallable = new FilterableCallable<>(objectClass, query);
        return FSFuture.fromCallable(filterableCallable);
    }

}
