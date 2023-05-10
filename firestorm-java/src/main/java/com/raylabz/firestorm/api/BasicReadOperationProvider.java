package com.raylabz.firestorm.api;

import java.util.List;

/**
 * An interface for basic operations.
 * @param <ExceptionType> The exception ocurring.
 */
public interface BasicReadOperationProvider <ExceptionType extends Exception> {

    /**
     * Retrieves a single object.
     * @param objectClass The object's class.
     * @param documentID The ID of the object.
     * @return Returns an object.
     * @param <T> The type of the object.
     * @throws ExceptionType thrown when a failure occurs.
     */
    <T> T get(final Class<T> objectClass, final String documentID) throws ExceptionType;

    /**
     * Retrieves all objects of a given class.
     * @param objectClass The class.
     * @return Returns a list.
     * @param <T> The type of obejcts.
     * @throws ExceptionType thrown when a failure occurs.
     */
    <T> List<T> list(final Class<T> objectClass) throws ExceptionType;

    /**
     * Retrieves all objects of a given class.
     * @param objectClass The class.
     * @param limit The number of documents to retrieve.
     * @return Returns a list.
     * @param <T> The type of obejcts.
     * @throws ExceptionType thrown when a failure occurs.
     */
    <T> List<T> list(final Class<T> objectClass, final int limit) throws ExceptionType;

}
