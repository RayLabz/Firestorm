package com.raylabz.firestorm;

import com.google.cloud.firestore.DocumentChange;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.NoSuchElementException;

/**
 * Models an object change that occured on a document in Firestore.
 * @param <T> The type of object.
 * @version 1.3.2
 */
public class ObjectChange<T> {

    /**
     * Represents a modification made on an object.
     */
    public enum Type {
        ADDED,
        MODIFIED,
        REMOVED
        ;

        /**
         * Converts a DocumentChange into the corresponding ObjectChange.Type.
         * @param t The document change type.
         * @return Returns a type.
         */
        public static Type fromDocumentChangeType(DocumentChange.Type t) {
            switch (t) {
                case ADDED:
                    return ADDED;
                case MODIFIED:
                    return MODIFIED;
                case REMOVED:
                    return REMOVED;
            }
            throw new NoSuchElementException("Invalid DocumentChange.Type provided");
        }

    }

    final T object;
    final QueryDocumentSnapshot document;
    final int oldIndex;
    final int newIndex;
    final Type type;

    /**
     * Constructs a new ObjectChange.
     * @param object The object returned.
     * @param document The object's document.
     * @param oldIndex The old index.
     * @param newIndex The new index.
     * @param type The type of change.
     */
    public ObjectChange(T object, QueryDocumentSnapshot document, int oldIndex, int newIndex, Type type) {
        this.object = object;
        this.document = document;
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
        this.type = type;
    }

    /**
     * Retrieves the object returned.
     * @return Returns an object.
     */
    public T getObject() {
        return object;
    }

    /**
     * Retrieves the updated object's document.
     * @return Returns a {@link QueryDocumentSnapshot}.
     */
    public QueryDocumentSnapshot getDocument() {
        return document;
    }

    /**
     * Retrieves the old index.
     * @return Returns an integer.
     */
    public int getOldIndex() {
        return oldIndex;
    }

    /**
     * Retrieves the new index.
     * @return Returns an integer.
     */
    public int getNewIndex() {
        return newIndex;
    }

    /**
     * Retrieves the change type.
     * @return Returns an {@link ObjectChange.Type}
     */
    public Type getType() {
        return type;
    }

}
