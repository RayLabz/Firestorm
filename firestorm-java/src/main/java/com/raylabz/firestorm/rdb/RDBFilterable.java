package com.raylabz.firestorm.rdb;

import com.google.firebase.database.*;
import com.raylabz.firestorm.api.Filterable;
import com.raylabz.firestorm.async.FSFuture;
import com.raylabz.firestorm.firestore.FSFilterable;
import com.raylabz.firestorm.rdb.callables.FilterableCallable;
import com.raylabz.firestorm.rdb.callables.GetSingleItemCallable;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * An object that enables quick use of RDB filters.
 * @author Nicos Kasenides
 * @version 2.0.0
 * @param <T> The type of objects filtered.
 */
public class RDBFilterable<T> extends Filterable<Query, T> {

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
    public RDBFilterable<T> startAt(double value, String key) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> startAt(String value, String key) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> startAt(boolean value, String key) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> startAt(double value) {
        query = query.startAt(value);
        return this;
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> startAt(String value) {
        query = query.startAt(value);
        return this;
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> startAt(boolean value) {
        query = query.startAt(value);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(double value) {
        query = query.endAt(value);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(String value) {
        query = query.endAt(value);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(boolean value) {
        query = query.endAt(value);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(double value, String key) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(String value, String key) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(boolean value, String key) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> equalTo(double value) {
        query = query.equalTo(value);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> equalTo(String value) {
        query = query.equalTo(value);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @return Returns a Query.
     */
    public RDBFilterable<T> equalTo(boolean value) {
        query = query.equalTo(value);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> equalTo(double value, String key) {
        query = query.equalTo(value, key);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> equalTo(String value, String key) {
        query = query.equalTo(value, key);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> equalTo(boolean value, String key) {
        query = query.equalTo(value, key);
        return this;
    }

    /**
     * Orders the results by child value.
     * @param path The path of the child (attribute name).
     * @return Returns a query.
     */
    public RDBFilterable<T> orderByChild(String path) {
        query = query.orderByChild(path);
        return this;
    }

    /**
     * Orders the results by key.
     * @return Returns a query.
     */
    public RDBFilterable<T> orderByKey() {
        query = query.orderByKey();
        return this;
    }

//    /**
//     * Orders the results by value.
//     * @return Returns a query.
//     */
//    public RDBFilterable<T> orderByValue() {
//        query.orderByValue();
//        return this;
//    }

    /**
     * Orders the results by priority.
     * @return Returns a query.
     */
    public RDBFilterable<T> orderByPriority() {
        query = query.orderByPriority();
        return this;
    }

    /**
     * Adds a listener to the object.
     * @param listener The listener.
     * @return Returns a query.
     */
    public RDBFilterable<T> addValueEventListener(ValueEventListener listener) {
        query.addValueEventListener(listener);
        return this;
    }

    /**
     * Adds a child listener to the object.
     * @param listener The listener.
     * @return Returns a query.
     */
    public RDBFilterable<T> addChildEventListener(ChildEventListener listener) {
        query.addChildEventListener(listener);
        return this;
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
