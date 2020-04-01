package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiStreamObserver;
import com.google.cloud.firestore.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * An object that enables quick use of Firestore filters without using additional DocumentReferences.
 *
 * @param <T> The type of objects this FirestormFilterable is able to interact with.
 */
public class FirestormFilterable<T> implements Filterable<T> {

    protected Query query;
    protected final Class<T> objectClass;

    /**
     * Instantiates a class of FirestormFilterable.
     *
     * @param query  The initial query of the filterable.
     * @param objectClass The type of objects this filterable can interact with.
     */
    public FirestormFilterable(Query query, final Class<T> objectClass) {
        this.query = query;
        this.objectClass = objectClass;
    }

    /**
     * Retrieves the current query.
     * @return Returns Query.
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Filters by value (equality).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereEqualTo(@Nonnull String field, @Nullable Object value) {
        query = query.whereEqualTo(field, value);
        return this;
    }

    /**
     * Filters by value by field path (equality).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value) {
        query = query.whereEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (less than).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereLessThan(@Nonnull String field, @Nonnull Object value) {
        query = query.whereLessThan(field, value);
        return this;
    }

    /**
     * Filters by value (less than).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereLessThan(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (less than or equal to).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereLessThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        query = query.whereLessThanOrEqualTo(field, value);
        return this;
    }

    /**
     * Filters by value (less than or equal to).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereLessThanOrEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (greater than).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereGreaterThan(@Nonnull String field, @Nonnull Object value) {
        query = query.whereGreaterThan(field, value);
        return this;
    }

    /**
     * Filters by value (greater than).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereGreaterThan(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (greater than or equal to).
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        query = query.whereGreaterThanOrEqualTo(field, value);
        return this;
    }

    /**
     * Filters by value (greater than or equal to).
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereGreaterThanOrEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by array field containing a value.
     * @param field The field.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereArrayContains(@Nonnull String field, @Nonnull Object value) {
        query = query.whereArrayContains(field, value);
        return this;
    }

    /**
     * Filters by array field containing a value.
     * @param fieldPath The field path.
     * @param value The value.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereArrayContains(fieldPath, value);
        return this;
    }

    /**
     * Filters by field containing any of a list of values.
     * @param field The field.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereArrayContainsAny(@Nonnull String field, @Nonnull List<?> values) {
        query = query.whereArrayContainsAny(field, values);
        return this;
    }

    /**
     * Filters by field containing any of a list of values.
     * @param fieldPath The field path.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereArrayContainsAny(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        query = query.whereArrayContainsAny(fieldPath, values);
        return this;
    }

    /**
     * Filters by an <u>array</u> field containing a list of values.
     * @param field The field.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereIn(@Nonnull String field, @Nonnull List<?> values) {
        query = query.whereIn(field, values);
        return this;
    }

    /**
     * Filters by an <u>array</u> field containing a list of values.
     * @param fieldPath The field path.
     * @param values The list of values.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        query = query.whereIn(fieldPath, values);
        return this;
    }

    /**
     * Orders results by a field.
     * @param field The field.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull String field) {
        query = query.orderBy(field);
        return this;
    }

    /**
     * Orders results by a field.
     * @param fieldPath The field path.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull FieldPath fieldPath) {
        query = query.orderBy(fieldPath);
        return this;
    }

    /**
     * Orders results by a field in a specified direction.
     * @param field The field.
     * @param direction The direction (ascending/descending) of ordering.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull String field, @Nonnull Query.Direction direction) {
        query = query.orderBy(field, direction);
        return this;
    }

    /**
     * Orders results by a field in a specified direction.
     * @param fieldPath The field path.
     * @param direction The direction (ascending/descending) of ordering.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull FieldPath fieldPath, @Nonnull Query.Direction direction) {
        query = query.orderBy(fieldPath, direction);
        return this;
    }

    /**
     * Limits the number of results.
     * @param limit The maximum number of results to fetch.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> limit(int limit) {
        query = query.limit(limit);
        return this;
    }

    /**
     * Offsets the results by a certain index.
     * @param offset The offset.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> offset(int offset) {
        query = query.offset(offset);
        return this;
    }

    /**
     * Specifies the starting point of a query.
     * @param snapshot The document snapshot to start retrieving the results from.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> startAt(@Nonnull DocumentSnapshot snapshot) {
        query = query.startAt(snapshot);
        return this;
    }

    /**
     * Specifies the starting point of a query.
     * @param fieldValues The field values to start the filtering results at.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> startAt(Object... fieldValues) {
        query = query.startAt(fieldValues);
        return this;
    }

    /**
     * Selects a specified set of fields from an object.
     * @param fields The fields to select.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> select(String... fields) {
        query = query.select(fields);
        return this;
    }

    /**
     * Selects a specified set of fields from an object.
     * @param fieldPaths The paths of the fields to select.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> select(FieldPath... fieldPaths) {
        query = query.select(fieldPaths);
        return this;
    }

    /**
     * Specifies the starting point of a query - after the specified document.
     * @param snapshot The document snapshot to start retrieving the results after.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> startAfter(@Nonnull DocumentSnapshot snapshot) {
        query = query.startAfter(snapshot);
        return this;
    }

    /**
     * Specifies the starting point of a query - after the spcified field values.
     * @param fieldValues The field values to starting retrieving the results after.
     * @return Returns a filterable.
     */
    public FirestormFilterable<T> startAfter(Object... fieldValues) {
        query = query.startAfter(fieldValues);
        return this;
    }

    /**
     * Specifies the ending point of a query (before this point)
     * @param snapshot The document snapshot to end the query before.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> endBefore(@Nonnull DocumentSnapshot snapshot) {
        query = query.endBefore(snapshot);
        return this;
    }

    /**
     * Specifies the ending point of a query (before this point).
     * @param fieldValues The field values to end the query before.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> endBefore(Object... fieldValues) {
        query = query.endBefore(fieldValues);
        return this;
    }

    /**
     * Specifies the ending point of a query.
     * @param fieldValues The field values to end the query at.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> endAt(Object... fieldValues) {
        query = query.endAt(fieldValues);
        return this;
    }

    /**
     * Specifies the ending point of a query.
     * @param snapshot The document snapshot the end the query at.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> endAt(@Nonnull DocumentSnapshot snapshot) {
        query = query.endAt(snapshot);
        return this;
    }

    /**
     * Streams the query to an observer.
     * @param responseObserver The observer to stream the query to.
     */
    public void stream(@Nonnull ApiStreamObserver<DocumentSnapshot> responseObserver) {
        query.stream(responseObserver);
    }

    /**
     * Retrieves the query snapshot.
     * @return Returns an ApiFuture of type QuerySnapshot.
     */
    @Nonnull
    public ApiFuture<QuerySnapshot> get() {
        return query.get();
    }

    /**
     * Adds an event listener to a snapshot.
     * @param listener The listener to add.
     * @return Returns a ListenerRegistration.
     */
    @Nonnull
    public ListenerRegistration addSnapshotListener(@Nonnull EventListener<QuerySnapshot> listener) {
        return query.addSnapshotListener(listener);
    }

    /**
     * Adds an event listener to a snapshot.
     * @param executor The executor of the event.
     * @param listener The listener to add.
     * @return Returns a ListenerRegistration.
     */
    @Nonnull
    public ListenerRegistration addSnapshotListener(@Nonnull Executor executor, @Nonnull EventListener<QuerySnapshot> listener) {
        return query.addSnapshotListener(executor, listener);
    }

    /**
     * Retrieves a hash code for the query.
     * @return Returns an integer hash code.
     */
    public int hashCode() {
        return query.hashCode();
    }

    /**
     * Fetches the results of a filterable.
     * @return An ArrayList containing the results of a filter.
     */
    public QueryResult<T> fetch() {
        ApiFuture<QuerySnapshot> future = query.get();
        try {
            final List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<>();
            String lastID = null;
            for (final QueryDocumentSnapshot document : documents) {
                T object = document.toObject(objectClass);
                documentList.add(object);
                lastID = document.getId();
            }
            if (documentList.isEmpty()) {
                return new QueryResult<>(documentList, null, null);
            }
            else {
                return new QueryResult<>(documentList, documents.get(documents.size() - 1), lastID);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
