# Firestorm: RDB Queries

## Synchronous

```java
List<Person> items = RDB.filter(MyClass.class)
        .whereEqualTo("age", 30)
        .fetch()
        .now();
```

## Asynchronous
```java
RDB.filter(MyClass.class)
        .whereEqualTo("age", 30)
        .fetch()
        .then(result -> {
            //TODO - Work with result
        });
```

## Query function reference

You can use RDB's original functions to filter, limit, order, or offset the retrieved values.

| **Method**                  | **Use**                                                                                  | **Params**                                                                             |
|-----------------------------|------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| `whereEqualTo             ` | Returns objects that have a field with a value equal to the given value.                 | Field name, value  Field path, value                                                   |
| `whereLessThan            ` | Returns objects that have a field with a value less than the given value.                | Field name, value  Field path, value                                                   |
| `whereLessThanOrEqualTo   ` | Returns objects that have a field with a value less than or equal to the given value.    | Field name, value  Field path, value                                                   |
| `whereGreaterThan         ` | Returns objects that have a field with a value greater than the given value.             | Field name, value  Field path, value                                                   |
| `whereGreaterThanOrEqualTo` | Returns objects that have a field with a value greater than or equal to the given value. | Field name, value  Field path, value                                                   |
| `orderBy                  ` | Returns objects ordered by a specific field.                                             | Field name  Field path  Field name, Direction of order  Field path, Direction of order |
| `orderByKey               ` | Returns objects ordered by key.                                                          | -                                                                                      |
| `orderByPriority          ` | Returns objects ordered by priority.                                                     | -                                                                                      |
| `limitToFirst             ` | Limits the number of results returned by the query from the start of the results.        | Limit (number)                                                                         |
| `limitToLast              ` | Limits the number of results returned by the query from the end of the results.          | Limit (number)                                                                         |
| `startAt                  ` | Starts the query at a specific document.                                                 | DocumentSnapshot  List of field values                                                 |
| `endAt                    ` | Ends the query at a specified document.                                                  | DocumentSnapshot  List of field values                                                 |
| `addChildEventListener    ` | Adds a snapshot listener to a child.                                                     | ChildEventListener                                                                     |
| `addValueEventListener    ` | Adds a snapshot listener to a value.                                                     | ValueEventListener                                                                     |
| `fetch                    ` | Fetches the results of the query directly.                                               | -                                                                                      |
