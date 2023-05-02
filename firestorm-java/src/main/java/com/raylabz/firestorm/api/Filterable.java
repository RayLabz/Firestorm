package com.raylabz.firestorm.api;

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
    public final QueryType getQuery() {
        return query;
    }

    /**
     * Retrieves the class of the objects handled by this filterable.
     * @return Returns a class.
     */
    public final Class<ObjectType> getObjectClass() {
        return objectClass;
    }

}
