package com.raylabz.firestorm;

import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.UnimplementedException;
import com.google.cloud.firestore.FieldPath;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Enables easy pagination.
 * @param <T> The type of objects returned by the paginator.
 */
public abstract class Paginator<T> {

    private final int DEFAULT_LIMIT = 10;

    private QueryDocumentSnapshot lastSnapshot = null;
    private Query query;
    private int limit = DEFAULT_LIMIT;

    /**
     * Instantiates a Paginator object for a certain class.
     * @param objectClass The type of objects returned by the Paginator.
     */
    public Paginator(Class<T> objectClass) {
        query = Firestorm.firestore.collection(objectClass.getSimpleName());
    }

    /**
     * Instantiates a Paginator object for a certain class and a certain results limit.
     * @param objectClass The type of objects returned by the Paginator.
     * @param limit The limit in number of results for each page.
     */
    public Paginator(Class<T> objectClass, int limit) {
        this(objectClass);
        this.limit = limit;
    }

    /**
     * Filters by value (equality).
     * @param field The field.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereEqualTo(@Nonnull String field, @Nullable Object value) {
        query = query.whereEqualTo(field, value);
        return this;
    }

    /**
     * Filters by value by field path (equality).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value) {
        query = query.whereEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (less than).
     * @param field The field.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereLessThan(@Nonnull String field, @Nonnull Object value) {
        query = query.whereLessThan(field, value);
        return this;
    }

    /**
     * Filters by value (less than).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereLessThan(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (less than or equal to).
     * @param field The field.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereLessThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        query = query.whereLessThanOrEqualTo(field, value);
        return this;
    }

    /**
     * Filters by value (less than or equal to).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereLessThanOrEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (greater than).
     * @param field The field.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereGreaterThan(@Nonnull String field, @Nonnull Object value) {
        query = query.whereGreaterThan(field, value);
        return this;
    }

    /**
     * Filters by value (greater than).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereGreaterThan(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (greater than or equal to).
     * @param field The field.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereGreaterThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        query = query.whereGreaterThanOrEqualTo(field, value);
        return this;
    }

    /**
     * Filters by value (greater than or equal to).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereGreaterThanOrEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by array field containing a value.
     * @param field The field.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereArrayContains(@Nonnull String field, @Nonnull Object value) {
        query = query.whereArrayContains(field, value);
        return this;
    }

    /**
     * Filters by array field containing a value.
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereArrayContains(fieldPath, value);
        return this;
    }

    /**
     * Filters by field containing any of a list of values.
     * @param field The field.
     * @param values The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereArrayContainsAny(@Nonnull String field, @Nonnull List<?> values) {
        query = query.whereArrayContainsAny(field, values);
        return this;
    }

    /**
     * Filters by field containing any of a list of values.
     * @param fieldPath The field path.
     * @param values The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereArrayContainsAny(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        query = query.whereArrayContainsAny(fieldPath, values);
        return this;
    }

    /**
     * Filters by an <u>array</u> field containing a list of values.
     * @param field The field.
     * @param values The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereIn(@Nonnull String field, @Nonnull List<?> values) {
        query = query.whereIn(field, values);
        return this;
    }

    /**
     * Filters by an <u>array</u> field containing a list of values.
     * @param fieldPath The field path.
     * @param values The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        query = query.whereIn(fieldPath, values);
        return this;
    }

    /**
     * Orders results by a field.
     * @param field The field.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> orderBy(@Nonnull String field) {
        query = query.orderBy(field);
        return this;
    }

    /**
     * Orders results by a field.
     * @param fieldPath The field path.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> orderBy(@Nonnull FieldPath fieldPath) {
        query = query.orderBy(fieldPath);
        return this;
    }

    /**
     * Orders results by a field in a specified direction.
     * @param field The field.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> orderBy(@Nonnull String field, @Nonnull Query.Direction direction) {
        query = query.orderBy(field, direction);
        return this;
    }

    /**
     * Orders results by a field in a specified direction.
     * @param fieldPath The field path.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> orderBy(@Nonnull FieldPath fieldPath, @Nonnull Query.Direction direction) {
        query = query.orderBy(fieldPath, direction);
        return this;
    }

    public ArrayList<T> getNext() {
        //TODO - Implement
        return null;

        //TODO Update last snapshot too...
    }

}
