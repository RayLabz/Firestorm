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
    public RDBFilterable<T> startAt(String key, double value) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> startAt(String key, String value) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values higher than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> startAt(String key, boolean value) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(String key, double value) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(String key, String value) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results to values lower than the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    public RDBFilterable<T> endAt(String key, boolean value) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    @Nonnull
    public RDBFilterable<T> whereEqualTo(@Nonnull String key, double value) {
        query = query.equalTo(value, key);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    @Nonnull
    public RDBFilterable<T> whereEqualTo(@Nonnull String key, @Nullable String value) {
        query = query.equalTo(value, key);
        return this;
    }

    /**
     * Limits the query results equal to the defined value.
     * @param value The value.
     * @param key The key.
     * @return Returns a Query.
     */
    @Nonnull
    public RDBFilterable<T> whereEqualTo(@Nonnull String key, boolean value) {
        query = query.equalTo(value, key);
        return this;
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public RDBFilterable<T> whereLessThan(@Nonnull String key, double value) {
        query = query.endAt(value - 1, key);
        return this;
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public RDBFilterable<T> whereLessThan(@Nonnull String key, String value) {
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
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public RDBFilterable<T> whereLessThanOrEqualTo(@Nonnull String key, double value) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public RDBFilterable<T> whereLessThanOrEqualTo(@Nonnull String key, @Nonnull String value) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Limits the query results to those less than the defined value.
     * @param key The key.
     * @param value The value.
     * @return Returns a Query.
     */
    @Nonnull
    public RDBFilterable<T> whereLessThanOrEqualTo(@Nonnull String key, boolean value) {
        query = query.endAt(value, key);
        return this;
    }

    /**
     * Filters by value (greater than).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public RDBFilterable<T> whereGreaterThan(@Nonnull String key, double value) {
        query = query.startAt(value + 1, key);
        return this;
    }

    /**
     * Filters by value (greater than).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public RDBFilterable<T> whereGreaterThan(@Nonnull String key, String value) {
        char c = 0;
        query = query.startAt(value + c, key);
        return this;
    }

    /**
     * Filters by value (greater than or equal to).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public RDBFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String key, double value) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Filters by value (greater than or equal to).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public RDBFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String key, String value) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Filters by value (greater than or equal to).
     * @param key The key.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public RDBFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String key, boolean value) {
        query = query.startAt(value, key);
        return this;
    }

    /**
     * Orders the results by child value.
     * @param key The key of the child (attribute name).
     * @return Returns a query.
     */
    public RDBFilterable<T> orderBy(String key) {
        query = query.orderByChild(key);
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
