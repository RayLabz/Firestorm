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
 * @param <T> The type of objects this FirestormFilterable is able to interact with.
 */
public class FirestormFilterable<T> {

    private final Query query;
    private final Class<T> aClass;

    /**
     * Instantiates a class of FirestormFilterable.
     * @param query The initial query of the filterable.
     * @param aClass The type of objects this filterable can interact with.
     */
    public FirestormFilterable(Query query, final Class<T> aClass) {
        this.query = query;
        this.aClass = aClass;
    }

    /**
     * Retrieves the current query.
     * @return Returns Query.
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Filters a given <b>field</b> that is equal to the given <b>value</b>.
     * @param field The field to filter.
     * @param value The value of the filter.
     * @return Returns a filterable.
     */
    @Nonnull
    public FirestormFilterable<T> whereEqualTo(@Nonnull String field, @Nullable Object value) {
        return new FirestormFilterable<T>(query.whereEqualTo(field, value), aClass);
    }

    //TODO - Continue documentation of methods.

    @Nonnull
    public FirestormFilterable<T> whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value) {
        return new FirestormFilterable<T>(query.whereEqualTo(fieldPath, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereLessThan(@Nonnull String field, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereLessThan(field, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereLessThan(fieldPath, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereLessThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereLessThanOrEqualTo(field, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereLessThanOrEqualTo(fieldPath, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereGreaterThan(@Nonnull String field, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereGreaterThan(field, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereGreaterThan(fieldPath, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereGreaterThanOrEqualTo(@Nonnull String field, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereGreaterThanOrEqualTo(field, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereGreaterThanOrEqualTo(fieldPath, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereArrayContains(@Nonnull String field, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereArrayContains(field, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        return new FirestormFilterable<T>(query.whereArrayContains(fieldPath, value), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereArrayContainsAny(@Nonnull String field, @Nonnull List<?> values) {
        return new FirestormFilterable<T>(query.whereArrayContainsAny(field, values), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereArrayContainsAny(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        return new FirestormFilterable<T>(query.whereArrayContainsAny(fieldPath, values), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereIn(@Nonnull String field, @Nonnull List<?> values) {
        return new FirestormFilterable<T>(query.whereIn(field, values), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        return new FirestormFilterable<T>(query.whereIn(fieldPath, values), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> orderBy(@Nonnull String field) {
        return new FirestormFilterable<T>(query.orderBy(field), aClass);
    }

    @Nonnull
    
    public FirestormFilterable<T> orderBy(@Nonnull FieldPath fieldPath) {
        return new FirestormFilterable<T>(query.orderBy(fieldPath), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull String field, @Nonnull Query.Direction direction) {
        return new FirestormFilterable<T>(query.orderBy(field, direction), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> orderBy(@Nonnull FieldPath fieldPath, @Nonnull Query.Direction direction) {
        return new FirestormFilterable<T>(query.orderBy(fieldPath, direction), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> limit(int limit) {
        return new FirestormFilterable<T>(query.limit(limit), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> offset(int offset) {
        return new FirestormFilterable<T>(query.offset(offset), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> startAt(@Nonnull DocumentSnapshot snapshot) {
        return new FirestormFilterable<T>(query.startAt(snapshot), aClass);
    }
    @Nonnull
    public FirestormFilterable<T> startAt(Object... fieldValues) {
        return new FirestormFilterable<T>(query.startAt(fieldValues), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> select(String... fields) {
        return new FirestormFilterable<T>(query.select(fields), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> select(FieldPath... fieldPaths) {
        return new FirestormFilterable<T>(query.select(fieldPaths), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> startAfter(@Nonnull DocumentSnapshot snapshot) {
        return new FirestormFilterable<T>(query.startAfter(snapshot), aClass);
    }

    
    public FirestormFilterable<T> startAfter(Object... fieldValues) {
        return new FirestormFilterable<T>(query.startAfter(fieldValues), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> endBefore(@Nonnull DocumentSnapshot snapshot) {
        return new FirestormFilterable<T>(query.endBefore(snapshot), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> endBefore(Object... fieldValues) {
        return new FirestormFilterable<T>(query.endBefore(fieldValues), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> endAt(Object... fieldValues) {
        return new FirestormFilterable<T>(query.endAt(fieldValues), aClass);
    }

    @Nonnull
    public FirestormFilterable<T> endAt(@Nonnull DocumentSnapshot snapshot) {
        return new FirestormFilterable<T>(query.endAt(snapshot), aClass);
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


    public boolean equals(Object obj) {
        return query.equals(obj);
    }

    public int hashCode() {
        return query.hashCode();
    }

    @Nonnull
    public Firestore getFirestore() {
        return query.getFirestore();
    }

    public ArrayList<T> fetch() {
        final Firestore firestore = query.getFirestore();
        ApiFuture<QuerySnapshot> future = firestore.collection(aClass.getSimpleName()).get();
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
