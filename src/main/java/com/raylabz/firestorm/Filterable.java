package com.raylabz.firestorm;

/**
 * Abstracts the fetch method, used by filtered query objects to fetch the results.
 * @param <T> The type of objects used in this filterable.
 */
public interface Filterable<T> {

    /**
     * Fetches the results of a filterable.
     * @return An ArrayList containing the results of a filter.
     */
    QueryResult<T> fetch();

}
