package com.raylabz.firestorm.api;

import com.google.cloud.firestore.*;
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
     * Retrieves the class of the objects handled by this filterable.
     * @return Returns a class.
     */
    public Class<ObjectType> getObjectClass() {
        return objectClass;
    }

    /**
     * Filters by value (equality).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereEqualTo(@Nonnull String field, @Nullable Object value);

    /**
     * Filters by value by field path (equality).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value);

    /**
     * Filters by value (less than).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereLessThan(@Nonnull String field, @Nonnull Object value);

    /**
     * Filters by value (less than).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value);

    /**
     * Filters by value (less than or equal to).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereLessThanOrEqualTo(@Nonnull String field, @Nonnull Object value);

    /**
     * Filters by value (less than or equal to).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value);

    /**
     * Filters by value (greater than).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereGreaterThan(@Nonnull String field, @Nonnull Object value);

    /**
     * Filters by value (greater than).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value);

    /**
     * Filters by value (greater than or equal to).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereGreaterThanOrEqualTo(@Nonnull String field, @Nonnull Object value);

    /**
     * Filters by value (greater than or equal to).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value);

    /**
     * Filters by array field containing a value.
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereArrayContains(@Nonnull String field, @Nonnull java.lang.Object value);

    /**
     * Filters by array field containing a value.
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull java.lang.Object value);

    /**
     * Filters by field containing any of a list of values.
     * @param field The field.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereArrayContainsAny(@Nonnull String field, @Nonnull List<?> values);

    /**
     * Filters by field containing any of a list of values.
     * @param fieldPath The field path.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereArrayContainsAny(@Nonnull FieldPath fieldPath, @Nonnull List<?> values);

    /**
     * Filters by an <u>array</u> field containing a list of values.
     * @param field The field.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereIn(@Nonnull String field, @Nonnull List<?> values);

    /**
     * Filters by an <u>array</u> field containing a list of values.
     * @param fieldPath The field path.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values);

    /**
     * Filters by an <u>array</u> field NOT containing a list of values.
     *
     * @param field  The field.
     * @param values The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereNotIn(@Nonnull String field, @Nonnull List<?> values);

    /**
     * Filters by an <u>array</u> field NOT containing a list of values.
     *
     * @param fieldPath The field path.
     * @param values    The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> whereNotIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values);

    /**
     * Orders results by a field.
     * @param field The field.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> orderBy(@Nonnull String field);

    /**
     * Orders results by a field.
     * @param fieldPath The field path.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> orderBy(@Nonnull FieldPath fieldPath);

    /**
     * Orders results by a field in a specified direction.
     * @param field The field.
     * @param direction The direction (ascending/descending) of ordering.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> orderBy(@Nonnull String field, @Nonnull Query.Direction direction);

    /**
     * Orders results by a field in a specified direction.
     * @param fieldPath The field path.
     * @param direction The direction (ascending/descending) of ordering.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> orderBy(@Nonnull FieldPath fieldPath, @Nonnull Query.Direction direction);

    /**
     * Limits the number of results.
     * @param limit The maximum number of results to fetch.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> limit(int limit);

    /**
     * Offsets the results by a certain index.
     * @param offset The offset.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> offset(int offset);

    /**
     * Specifies the starting point of a query.
     * @param snapshot The document snapshot to start retrieving the results from.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> startAt(@Nonnull DocumentSnapshot snapshot);

    /**
     * Specifies the starting point of a query.
     * @param fieldValues The field values to start the filtering results at.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> startAt(Object... fieldValues);

    /**
     * Selects a specified set of fields from an object.
     * @param fields The fields to select.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> select(String... fields);

    /**
     * Selects a specified set of fields from an object.
     * @param fieldPaths The paths of the fields to select.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> select(FieldPath... fieldPaths);

    /**
     * Specifies the starting point of a query - after the specified document.
     * @param snapshot The document snapshot to start retrieving the results after.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> startAfter(@Nonnull DocumentSnapshot snapshot);

    /**
     * Specifies the starting point of a query - after the spcified field values.
     * @param fieldValues The field values to starting retrieving the results after.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> startAfter(Object... fieldValues);

    /**
     * Specifies the ending point of a query (before this point)
     * @param snapshot The document snapshot to end the query before.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> endBefore(@Nonnull DocumentSnapshot snapshot);

    /**
     * Specifies the ending point of a query to a specific snapshot.
     * @param fieldValues The field values to end the query at.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> endBefore(java.lang.Object... fieldValues);

    /**
     * Specifies the ending point of a query to a specific snapshot.
     * @param fieldValues The field values to end the query at.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> endAt(java.lang.Object... fieldValues);

    /**
     * Specifies the ending point of a query to a specific snapshot.
     * @param snapshot The document snapshot the end the query at.
     * @return Returns a filterable.
     */
    @Nonnull
    public abstract Filterable<QueryType,ObjectType> endAt(@Nonnull DocumentSnapshot snapshot);

}
