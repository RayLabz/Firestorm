package com.raylabz.firestorm.rdb;

import com.google.firebase.database.Query;
import com.raylabz.firestorm.api.Filterable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RDBFilterable<T> extends Filterable<Query,T> {

    /**
     * Constructs a new filterable object
     *
     * @param query       The query object used for filtering.
     * @param objectClass The object class.
     */
    public RDBFilterable(Query query, Class<T> objectClass) {
        super(query, objectClass);
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereEqualTo(@Nonnull String field, @Nullable Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereLessThan(@Nonnull String field, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereLessThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereGreaterThan(@Nonnull String field, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereArrayContains(@Nonnull String field, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereArrayContainsAny(@Nonnull String field, @Nonnull List<?> values) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereArrayContainsAny(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereIn(@Nonnull String field, @Nonnull List<?> values) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereNotIn(@Nonnull String field, @Nonnull List<?> values) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> whereNotIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> orderBy(@Nonnull String field) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> orderBy(@Nonnull FieldPath fieldPath) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> orderBy(@Nonnull String field, @Nonnull com.google.cloud.firestore.Query.Direction direction) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> orderBy(@Nonnull FieldPath fieldPath, @Nonnull com.google.cloud.firestore.Query.Direction direction) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> limit(int limit) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> offset(int offset) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> startAt(@Nonnull DocumentSnapshot snapshot) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> startAt(Object... fieldValues) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> select(String... fields) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> select(FieldPath... fieldPaths) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> startAfter(@Nonnull DocumentSnapshot snapshot) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> startAfter(Object... fieldValues) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> endBefore(@Nonnull DocumentSnapshot snapshot) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> endBefore(Object... fieldValues) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> endAt(Object... fieldValues) {
        return null;
    }

    @Nonnull
    @Override
    public RDBFilterable<T> endAt(@Nonnull DocumentSnapshot snapshot) {
        return null;
    }
}
