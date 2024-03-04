# Firestorm: Firestore Queries

## Synchronous

```java
FSQueryResult<Person> filteredItems = FS.filter(Person.class)
        .whereEqualTo("age", 30)
        .fetch()
        .now();

ArrayList<Person> items = filteredItems.getItems(); //Get items
QueryDocumentSnapshot snapshot = filteredItems.getSnapshot(); //Get the query snapshot
String lastDocumentID = filteredItems.getLastDocumentID(); //Get the ID of the last document in the list
```

## Asynchronous
```java
FS.filter(Person.class)
        .whereEqualTo("age", 30)
        .fetch()
        .then(result -> {
            ArrayList<Person> items = result.getItems(); //Get items
            QueryDocumentSnapshot snapshot = result.getSnapshot(); //Get the query snapshot
            String lastDocumentID = result.getLastDocumentID(); //Get the ID of the last document in the list
        });
```

## Query function reference

You can use Firestore's original functions to filter, limit, order, or offset the retrieved values.

| **Method**                | **Use**                                                                                  | **Params**                                                                             |
|---------------------------|------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------|
| `whereEqualTo             ` | Returns objects that have a field with a value equal to the given value.                 | Field name, value  Field path, value                                                   |
| `whereLessThan            ` | Returns objects that have a field with a value less than the given value.                | Field name, value  Field path, value                                                   |
| `whereLessThanOrEqualTo   ` | Returns objects that have a field with a value less than or equal to the given value.    | Field name, value  Field path, value                                                   |
| `whereGreaterThan         ` | Returns objects that have a field with a value greater than the given value.             | Field name, value  Field path, value                                                   |
| `whereGreaterThanOrEqualTo` | Returns objects that have a field with a value greater than or equal to the given value. | Field name, value  Field path, value                                                   |
| `whereArrayContains       ` | Returns objects that have a field (array) containing the given value.                    | Field name, value  Field path, value                                                   |
| `whereArrayContainsAny    ` | Returns objects that have a field (array) containing any of a given list of values.      | Field name, list of values  Field path, list of values                                 |
| `whereIn                  ` | Returns objects that have a field containing any of a given list of values.              | Field name, list of values  Field path, list of values                                 |
| `whereNotIn               ` | Returns objects of which a field does not contain any of a given list of values.         | Field name, list of values  Field path, list of values                                 |
| `orderBy                  ` | Returns objects ordered by a specific field.                                             | Field name  Field path  Field name, Direction of order  Field path, Direction of order |
| `limit                    ` | Limits the number of results returned by the query.                                      | Limit (number)                                                                         |
| `offset                   ` | Offsets the query's start position by a given amount.                                    | Offset(number)                                                                         |
| `startAt                  ` | Starts the query at a specific document.                                                 | DocumentSnapshot  List of field values                                                 |
| `select                   ` | Selects only specific fields to return from an object.                                   | List of fields  List of field paths                                                    |
| `startAfter               ` | Starts the query after a specified document.                                             | DocumentSnapshot  List of field values                                                 |
| `endBefore                ` | Ends the query before a specified document.                                              | DocumentSnapshot  List of field values                                                 |
| `endAt                    ` | Ends the query at a specified document.                                                  | DocumentSnapshot  List of field values                                                 |
| `stream                   ` | Streams the query to an observer.                                                        | ApiStreamObserver                                                                      |
| `get                      ` | Retrieves a snapshot of the query.                                                       | -                                                                                      |
| `addSnapshotListener      ` | Adds a snapshot listener to the query.                                                   | EventListener  Executor and EventListener                                              |
| `hashCode                 ` | Retrieves the hash code of the query.                                                    | -                                                                                      |
| `fetch                    ` | Fetches the results of the query directly.                                               | -                                                                                      |
