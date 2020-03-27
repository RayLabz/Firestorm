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
    protected final Class<T> aClass;

    /**
     * Instantiates a class of FirestormFilterable.
     *
     * @param query  The initial query of the filterable.
     * @param aClass The type of objects this filterable can interact with.
     */
    public FirestormFilterable(Query query, final Class<T> aClass) {
        this.query = query;
        this.aClass = aClass;
    }

    /**
     * Retrieves the current query.
     *
     * @return Returns Query.
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Filters a given <b>field</b> that is equal to the given <b>value</b>.
     *
     * @param field The field to filter.
     * @param value The value of the filter.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereEqualTo(@Nonnull String field, @Nullable Object value) {
        query = query.whereEqualTo(field, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value) {
        query = query.whereEqualTo(fieldPath, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereLessThan(@Nonnull String field, @Nonnull Object value) {
        query = query.whereLessThan(field, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereLessThan(fieldPath, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereLessThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        query = query.whereLessThanOrEqualTo(field, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereLessThanOrEqualTo(fieldPath, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereGreaterThan(@Nonnull String field, @Nonnull Object value) {
        query = query.whereGreaterThan(field, value);
        return this;
    }

    @Nonnull

    public FirestormFilterable<T> whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereGreaterThan(fieldPath, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        query = query.whereGreaterThanOrEqualTo(field, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereGreaterThanOrEqualTo(fieldPath, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereArrayContains(@Nonnull String field, @Nonnull Object value) {
        query = query.whereArrayContains(field, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereArrayContains(fieldPath, value);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereArrayContainsAny(@Nonnull String field, @Nonnull List<?> values) {
        query = query.whereArrayContainsAny(field, values);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereArrayContainsAny(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        query = query.whereArrayContainsAny(fieldPath, values);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereIn(@Nonnull String field, @Nonnull List<?> values) {
        query = query.whereIn(field, values);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        query = query.whereIn(fieldPath, values);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull String field) {
        query = query.orderBy(field);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull FieldPath fieldPath) {
        query = query.orderBy(fieldPath);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull String field, @Nonnull Query.Direction direction) {
        query = query.orderBy(field, direction);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull FieldPath fieldPath, @Nonnull Query.Direction direction) {
        query = query.orderBy(fieldPath, direction);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> limit(int limit) {
        query = query.limit(limit);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> offset(int offset) {
        query = query.offset(offset);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> startAt(@Nonnull DocumentSnapshot snapshot) {
        query = query.startAt(snapshot);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> startAt(Object... fieldValues) {
        query = query.startAt(fieldValues);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> select(String... fields) {
        query = query.select(fields);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> select(FieldPath... fieldPaths) {
        query = query.select(fieldPaths);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> startAfter(@Nonnull DocumentSnapshot snapshot) {
        query = query.startAfter(snapshot);
        return this;
    }


    public FirestormFilterable<T> startAfter(Object... fieldValues) {
        query = query.startAfter(fieldValues);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> endBefore(@Nonnull DocumentSnapshot snapshot) {
        query = query.endBefore(snapshot);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> endBefore(Object... fieldValues) {
        query = query.endBefore(fieldValues);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> endAt(Object... fieldValues) {
        query = query.endAt(fieldValues);
        return this;
    }

    @Nonnull
    public FirestormFilterable<T> endAt(@Nonnull DocumentSnapshot snapshot) {
        query = query.endAt(snapshot);
        return this;
    }

    public void stream(@Nonnull ApiStreamObserver<DocumentSnapshot> responseObserver) {
        query.stream(responseObserver);
    }

    @Nonnull
    public ApiFuture<QuerySnapshot> get() {
        return query.get();
    }

    @Nonnull
    public ListenerRegistration addSnapshotListener(@Nonnull EventListener<QuerySnapshot> listener) {
        return query.addSnapshotListener(listener);
    }

    @Nonnull
    public ListenerRegistration addSnapshotListener(@Nonnull Executor executor, @Nonnull EventListener<QuerySnapshot> listener) {
        return query.addSnapshotListener(executor, listener);
    }

    public int hashCode() {
        return query.hashCode();
    }

    /**
     * Fetches the results of a filterable.
     * @return An ArrayList containing the results of a filter.
     */
    public ArrayList<T> fetch() {
        ApiFuture<QuerySnapshot> future = query.get();
        try {
            List<QueryDocumentSnapshot> documents = null;
            documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<T>();
            for (final QueryDocumentSnapshot document : documents) {
                Object object = document.toObject(aClass);
                documentList.add((T) object);
            }
            return documentList;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
