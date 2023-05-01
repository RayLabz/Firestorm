package com.raylabz.firestorm.api;

import com.google.cloud.firestore.*;
import com.google.storage.v2.Object;
import com.raylabz.firestorm.firestore.FSFilterable;
import com.raylabz.firestorm.firestore.FSQueryResult;
import com.raylabz.firestorm.async.FSFuture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Abstracts filtering functionality.
 * @param <ObjectType> The type of object being filtered.
 * @param <QueryType> The type of the query objects being used.
 * @author Nicos Kasenides
 * @version 2.0.0
 */
public abstract class Filterable<QueryType, ObjectType> {

    protected QueryType query;

    protected final Class<ObjectType> objectClass;

    /**
     * Constructs a new filterable object
     * @param query The query object used for filtering.
     * @param objectClass The object class.
     */
    public Filterable(QueryType query, Class<ObjectType> objectClass) {
        this.query = query;
        this.objectClass = objectClass;
    }

    /**
     * Retrieves the current query.
     * @return Returns Query.
     */
    public QueryType getQuery() {
        return query;
    }

    /**
     * Filters by value (equality).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereEqualTo(@Nonnull String field, @Nullable Object value);

    /**
     * Filters by value by field path (equality).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value);

    /**
     * Filters by value (less than).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereLessThan(@Nonnull String field, @Nonnull Object value);

    /**
     * Filters by value (less than).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value);

    /**
     * Filters by value (less than or equal to).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereLessThanOrEqualTo(@Nonnull String field, @Nonnull Object value);

    /**
     * Filters by value (less than or equal to).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value);

    /**
     * Filters by value (greater than).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereGreaterThan(@Nonnull String field, @Nonnull Object value);

    /**
     * Filters by value (greater than).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value);

    /**
     * Filters by value (greater than or equal to).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereGreaterThanOrEqualTo(@Nonnull String field, @Nonnull Object value);

    /**
     * Filters by value (greater than or equal to).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value);

    /**
     * Filters by array field containing a value.
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereArrayContains(@Nonnull String field, @Nonnull java.lang.Object value);

    /**
     * Filters by array field containing a value.
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull java.lang.Object value);

    /**
     * Filters by field containing any of a list of values.
     * @param field The field.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereArrayContainsAny(@Nonnull String field, @Nonnull List<?> values);

    /**
     * Filters by field containing any of a list of values.
     * @param fieldPath The field path.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereArrayContainsAny(@Nonnull FieldPath fieldPath, @Nonnull List<?> values);

    /**
     * Filters by an <u>array</u> field containing a list of values.
     * @param field The field.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereIn(@Nonnull String field, @Nonnull List<?> values);

    /**
     * Filters by an <u>array</u> field containing a list of values.
     * @param fieldPath The field path.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values);

    /**
     * Filters by an <u>array</u> field NOT containing a list of values.
     *
     * @param field  The field.
     * @param values The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereNotIn(@Nonnull String field, @Nonnull List<?> values);

    /**
     * Filters by an <u>array</u> field NOT containing a list of values.
     *
     * @param fieldPath The field path.
     * @param values    The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> whereNotIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values);

    /**
     * Orders results by a field.
     * @param field The field.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract FSFilterable<ObjectType> orderBy(@Nonnull String field);

    //TODO - Add JavaDoc

    @Nonnull
    public abstract FSFilterable<ObjectType> orderBy(@Nonnull FieldPath fieldPath);

    @Nonnull
    public abstract FSFilterable<ObjectType> orderBy(@Nonnull String field, @Nonnull Query.Direction direction);

    @Nonnull
    public abstract FSFilterable<ObjectType> orderBy(@Nonnull FieldPath fieldPath, @Nonnull Query.Direction direction);

    @Nonnull
    public abstract FSFilterable<ObjectType> limit(int limit);

    @Nonnull
    public abstract FSFilterable<ObjectType> offset(int offset);

    @Nonnull
    public abstract FSFilterable<ObjectType> startAt(@Nonnull DocumentSnapshot snapshot);

    @Nonnull
    public abstract FSFilterable<ObjectType> startAt(java.lang.Object... fieldValues);

    @Nonnull
    public abstract FSFilterable<ObjectType> select(String... fields);

    @Nonnull
    public abstract FSFilterable<ObjectType> select(FieldPath... fieldPaths);

    @Nonnull
    public abstract FSFilterable<ObjectType> startAfter(@Nonnull DocumentSnapshot snapshot);

    @Nonnull
    public abstract FSFilterable<ObjectType> startAfter(java.lang.Object... fieldValues);

    @Nonnull
    public abstract FSFilterable<ObjectType> endBefore(@Nonnull DocumentSnapshot snapshot);

    @Nonnull
    public abstract FSFilterable<ObjectType> endBefore(java.lang.Object... fieldValues);

    @Nonnull
    public abstract FSFilterable<ObjectType> endAt(java.lang.Object... fieldValues);

    @Nonnull
    public abstract FSFilterable<ObjectType> endAt(@Nonnull DocumentSnapshot snapshot);

    @Nonnull
    public abstract ListenerRegistration addSnapshotListener(@Nonnull EventListener<QuerySnapshot> listener);

    public abstract FSFuture<FSQueryResult<ObjectType>> fetch();

}
