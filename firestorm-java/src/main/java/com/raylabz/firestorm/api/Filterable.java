package com.raylabz.firestorm.api;

import com.raylabz.firestorm.firestore.FSQueryResult;
import com.raylabz.firestorm.async.FSFuture;

/**
 * Abstracts the fetch method, used by filtered query objects to fetch the results.
 * @param <T> The type of objects used in this filterable.
 * @author Nicos Kasenides
 * @version 1.0.0
 */
public interface Filterable<T> {

    /**
     * Fetches the results of a filterable.
     * @return An ArrayList containing the results of a filter.
     */
    FSFuture<FSQueryResult<T>> fetch();

}
