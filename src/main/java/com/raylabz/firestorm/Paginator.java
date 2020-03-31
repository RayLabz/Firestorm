package com.raylabz.firestorm;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.raylabz.firestorm.exception.FirestormException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Enables easy pagination.
 *
 * @param <T> The type of objects returned by the paginator.
 */
public class Paginator<T> implements Filterable<T> {

    private final int DEFAULT_LIMIT = 10;
    private Class<T> objectClass;
    private String lastDocumentID = null;
    private Query query;
    private int limit = DEFAULT_LIMIT;

    /**
     * Instantiates a Paginator object for a certain class.
     *
     * @param objectClass The type of objects returned by the Paginator.
     */
    private Paginator(Class<T> objectClass, final String lastDocumentID) {
        query = Firestorm.firestore.collection(objectClass.getSimpleName());
        this.objectClass = objectClass;
        this.lastDocumentID = lastDocumentID;
    }

    /**
     * Instantiates a Paginator object for a certain class and a certain results limit.
     *
     * @param objectClass The type of objects returned by the Paginator.
     * @param limit       The limit in number of results for each page.
     */
    private Paginator(Class<T> objectClass, final String lastDocumentID, int limit) {
        this(objectClass, lastDocumentID);
        this.limit = limit;
    }

    public static <T> Paginator<T> init(Class<T> objectClass, final String lastDocumentID, final int limit) {
        return new Paginator<>(objectClass, lastDocumentID, limit);
    }

    public static <T> Paginator<T> init(Class<T> objectClass, final String lastDocumentID) {
        return new Paginator<>(objectClass, lastDocumentID);
    }

    /**
     * Filters by value (equality).
     *
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
     *
     * @param fieldPath The field path.
     * @param value     The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereEqualTo(@Nonnull FieldPath fieldPath, @Nullable Object value) {
        query = query.whereEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (less than).
     *
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
     *
     * @param fieldPath The field path.
     * @param value     The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereLessThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereLessThan(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (less than or equal to).
     *
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
     *
     * @param fieldPath The field path.
     * @param value     The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereLessThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereLessThanOrEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (greater than).
     *
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
     *
     * @param fieldPath The field path.
     * @param value     The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereGreaterThan(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereGreaterThan(fieldPath, value);
        return this;
    }

    /**
     * Filters by value (greater than or equal to).
     *
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
     *
     * @param fieldPath The field path.
     * @param value     The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereGreaterThanOrEqualTo(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereGreaterThanOrEqualTo(fieldPath, value);
        return this;
    }

    /**
     * Filters by array field containing a value.
     *
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
     *
     * @param fieldPath The field path.
     * @param value     The value.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereArrayContains(@Nonnull FieldPath fieldPath, @Nonnull Object value) {
        query = query.whereArrayContains(fieldPath, value);
        return this;
    }

    /**
     * Filters by field containing any of a list of values.
     *
     * @param field  The field.
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
     *
     * @param fieldPath The field path.
     * @param values    The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereArrayContainsAny(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        query = query.whereArrayContainsAny(fieldPath, values);
        return this;
    }

    /**
     * Filters by an <u>array</u> field containing a list of values.
     *
     * @param field  The field.
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
     *
     * @param fieldPath The field path.
     * @param values    The list of values.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> whereIn(@Nonnull FieldPath fieldPath, @Nonnull List<?> values) {
        query = query.whereIn(fieldPath, values);
        return this;
    }

    /**
     * Orders results by a field.
     *
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
     *
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
     *
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
     *
     * @param fieldPath The field path.
     * @return Returns a Paginator.
     */
    @Nonnull
    public Paginator<T> orderBy(@Nonnull FieldPath fieldPath, @Nonnull Query.Direction direction) {
        query = query.orderBy(fieldPath, direction);
        return this;
    }


    @Override
    public QueryResult<T> fetch() {

        //If there is a last document, set the query to start after it:
        if (lastDocumentID != null) {
            DocumentSnapshot lastDocumentSnapshot;
            try {
                DocumentReference lastDocumentReference = Firestorm.firestore.collection(objectClass.getSimpleName()).document(lastDocumentID);
                ApiFuture<DocumentSnapshot> future = lastDocumentReference.get();
                lastDocumentSnapshot = future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new FirestormException(e);
            }
            if (lastDocumentSnapshot != null) {
                query = query.startAfter(lastDocumentSnapshot);
            }
        }

        //Query limits:
        query = query.limit(limit);

        //Run the query and return the results:
        ApiFuture<QuerySnapshot> future = query.get();
        try {
            final List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            ArrayList<T> documentList = new ArrayList<T>();
            String lastID = null;
            for (final QueryDocumentSnapshot document : documents) {
                T object = document.toObject(objectClass);
                documentList.add((T) object);
                lastID = document.getId();
            }
            return new QueryResult<T>(documentList, documents.get(documents.size() - 1), lastID); //TODO - If documents list has size 0??
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

}
