package com.raylabz.firestorm;

import java.util.ArrayList;

public interface Filterable<T> {

    /**
     * Fetches the results of a filterable.
     * @return An ArrayList containing the results of a filter.
     */
    QueryResult<T> fetch();

}
