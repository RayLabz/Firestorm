# Firestorm - Realtime Database API Guide ⚡

This guide shows examples on how to use Firestorm's Realtime database API.
It covers basic operations such as creating, reading, updating, and deleting documents, as well as more advanced features
like querying, real-time updates, and more.

For the full code documentation, please refer to the [Firestorm API Reference](https://raylabz.github.io/Firestorm/flutter-api/).

## Contents

- [Initialization](#initialization)
- [Basic Operations](#basic-operations)
    - [Create](#creating-objects)
    - [Get](#getting-objects)
    - [Update](#updating-objects)
    - [Delete](#deleting-objects)
    - [List](#listing-objects)
    - [Exist](#checking-existence-of-objects)
- [Queries & Filtering](#queries--filtering)
    - [Query Examples](#query-examples)
    - [Query Functions Reference](#query-functions-reference)
- [Real-time Listeners](#real-time-listeners)
- [Subcollections](#subcollections)
- [Pagination](#pagination)
- [References](#references)
- [Configuration options](#configuration-options)
    - [Caching](#caching)
    - [Using the emulator](#using-the-emulator)
    - [Shutting down the database instance](#shutting-down-the-realtime-database-instance)
- [Using the Realtime database API](#using-the-realtime-database-api)
---

## Initialization
To use the Firestorm Realtime Database API, you need to initialize it first using `RDB.init()`.
This registers your classes for serialization/deserialization and provides static safety checks.
```dart
await RDB.init();
```

> [!NOTE]
> This operation is asynchronous and should be called before using any other Firestorm API methods.
> It is recommended to call this in the `main()` function of your app, before running any other code.

Alternatively, you can initialize Firestorm with a custom `FirebaseOptions` configuration:
```dart
await RDB.init(options);
```

[Back to contents](#contents)

## Basic Operations

> [!CAUTION]
> A lot of operations are asynchronous and return `Future<?>`. In these cases, you may choose
> whether to `await` _(blocking)_ the operation, use then `then` or `catchError` _(non-blocking)_,
> or ignore the result entirely.

### Creating objects

#### Single object using `create.one()`:
```dart
await RDB.create.one(person);
```

#### Multiple objects (list) using `create.many()`:
```dart
await RDB.create.many(people);
```

[Back to contents](#contents)

---

### Getting objects
#### Single object using `get.one()`:
```dart
Person person = await RDB.get.one<Person>("person-id");
```

#### Multiple objects (list) using `get.many()`:
```dart
List<Person> people = await RDB.get.many<Person>(["person-id-1", "person-id-2"]);
```

[Back to contents](#contents)

---

### Updating objects
#### Single object using `update.one()`:
```dart
await RDB.update.one(person);
```

#### Multiple objects (list) using `update.many()`:
```dart
await RDB.update.many(people);
```

[Back to contents](#contents)

---

### Deleting objects
#### Single object using `delete.one()`:
```dart
await RDB.delete.one(person);
```

#### Single object using its ID with `delete.oneWithID()`:
```dart
await RDB.delete.oneWithID(Person, "person-id");
```

#### Multiple objects (list) using `delete.many()`:
```dart
await RDB.delete.many(people);
```

#### Many objects using their ID with `delete.manyWithID()`:
```dart
await RDB.delete.manyWithID(Person, ["person-id-1", "person-id-2"]);
```

#### All objects of a type using `delete.all()`:
```dart
await RDB.delete.all(Person, iAmSure: true);
```

> [!NOTE]
> The `iAmSure: true` parameter is required to prevent accidental deletion of all objects of a type.

[Back to contents](#contents)

---

### Listing objects

#### Up to 10 objects of a type using `list.ofClass()`:
```dart
List<Person> people = await RDB.list.ofClass<Person>();
```

#### With optional custom limit:
```dart
List<Person> people = await RDB.list.ofClass<Person>(limit: 5);
```

#### All objects of a type using `list.allOfClass()`:
```dart
List<Person> people = await RDB.list.allOfClass<Person>();
```

[Back to contents](#contents)

---

### Checking existence of objects

#### Single object using `exists.one()`:
```dart
bool exists = await RDB.exists.one<Person>(person);
```

#### Single object using its ID `exists.oneWithID()`:
```dart
bool exists = await RDB.exists.oneWithID(Person, "person-id");
```

[Back to contents](#contents)

---

## Queries & Filtering

### Query examples

#### Basic query to filter objects by a field value:

```dart
//Query:
RDBQueryResult<Person> result = await RDB.list.filter<Person>(Person)
        .equalTo(30, field: 'age')
        .fetch();

//Obtain result:
List<Person> people = result.items;
```

#### Query with multiple conditions:

```dart
//Query:
RDBQueryResult<Person> result = await RDB.list.filter<Person>(Person)
        .equalTo(30, field: 'age')
        .equalTo(1.70, field: 'height')
        .orderByChild("age")
        .fetch();

//Obtain result:
List<Person> people = result.items;
```

> [!NOTE]
> You can chain multiple query functions to build complex queries.

> [!NOTE]
> Sorting/ordering queries are only supported for fields that are indexed in the Realtime Database.
> If you encounter an error about missing indexes, you can create the required index in the Realtime Database console.
> For more information, refer to the [Realtime Database documentation](https://firebase.google.com/docs/database/flutter/structure-data#data_indexing).

> [!NOTE]
> Sorting functions such as `orderByChild()` must be used before `limitToFirst()`, `limitToLast()`, and after any `equal` clauses.

### Query functions reference
| Function | Description |
| --- | --- |
| `equalTo(value, {String? field})` | Filters objects where the specified field is equal to the given value. |
| `orderByChild(String field)` | Orders the results by the specified child field. |
| `orderByKey()` | Orders the results by the key of the objects. |
| `orderByValue()` | Orders the results by the value of the objects. |
| `limitToFirst(int limit)` | Limits the results to the first `limit` number of objects. |
| `limitToLast(int limit)` | Limits the results to the last `limit` number of objects. |
| `startAt(value, {String? field})` | Starts the results at the specified value for the given field. |
| `endAt(value, {String? field})` | Ends the results at the specified value for the given field. |
| `fetch()` | Executes the query and returns the results as a `RDBQueryResult<T>`. |


[Back to contents](#contents)

---

## Real-time Listeners

### Listen to a single object:
```dart
RDB.listen.toObject<Person>(
  person, 
  onCreate: (object) {
    print("Created: ${object.id}"); //TODO
  },
  onChange: (object) {
    print("Changed: ${object.id}"); //TODO
  },
  onDelete: () {
    print("Deleted"); //TODO
  }
);
```

### Listen to a list of objects:
```dart
RDB.listen.toObjects<Person>(
  people, 
  onCreate: (object) {
    print("Created: ${object.id}"); //TODO
  },
  onChange: (object) {
    print("Changed: ${object.id}"); //TODO
  },
  onDelete: () {
    print("Deleted"); //TODO
  }
);
```

### Listen to an object using its ID:
```dart
RDB.listen.toID<Person>(
  Person, "person-id", 
  onCreate: (object) {
    print("Created: ${object.id}"); //TODO
  },
  onChange: (object) {
    print("Changed: ${object.id}"); //TODO
  },
  onDelete: () {
    print("Deleted"); //TODO
  }
);
```

### Listen to a list of objects using IDs:
```dart
RDB.listen.toIDs<ComputingStudent>(
  ComputingStudent, ["person1-id", "person2-id"], 
  onCreate: (object) {
    print("Created: ${object.id}"); //TODO
  },
  onChange: (object) {
    print("Changed: ${object.id}"); //TODO
  },
  onDelete: () {
    print("Deleted"); //TODO
  }
);
```

### Listen to all objects of a data type:
```dart
RDB.listen.toType<Person>(
  Person,
  onCreate: (object) {
    print("Created $object"); //TODO
  },
  onChange: (object) {
    print("Changed: $object"); //TODO
  },
  onDelete: (object) {
    print("Deleted $object"); //TODO
  },
);
```

[Back to contents](#contents)

---

## Subcollections

You can access subcollections using the `subcollection` attribute, which is available for most calls:

### Example: Creating an object in a subcollection
```dart
await RDB.create.one(person, subcollection: "friends");
```

### Example: Getting an object from a subcollection
```dart
Person person = await RDB.get.one<Person>("person-id", subcollection: "friends");
```

**Firestorm offers a way to access subcollections in a variety of other methods.**

> [!NOTE]
> Subcollections are useful for organizing related data as part of a specific class.
> They can be used as an alternative to indexing or filtering data, to provide a quicker
> way to access related objects without needing to perform complex queries.

[Back to contents](#contents)

---

## Pagination

> [!NOTE]
> Pagination is not supported by Firestorm's RDB API in the current version. It is planned for a future release.

[Back to contents](#contents)

---

## References

You can create references to objects or entire classes using the `RDB.reference` method.
This returns a traditional Firestore reference that can be used to access the object or class.

### Reference to single document with class and ID
```dart
DatabaseReference reference = RDB.reference.documentFromID(RDB.constructPathForClassAndID(Person, ["person-id"]));
```

### Reference to single document with object
```dart
DatabaseReference reference = RDB.reference.documentFromObject(person);
```

### Reference to a single document with path
```dart
DatabaseReference reference = RDB.reference.documentFromPath("Person/person-id");
```

### Reference to a collection with class
```dart
DatabaseReference reference = RDB.reference.collection(Person);
```

[Back to contents](#contents)

---

## Configuration options
Firestorm provides several configuration options to customize its behavior.

### Caching
You can enable or disable caching for Realtime Database operations using `RDB.enableCaching()` or `RDB.disableCaching()`:
```dart
RDB.enableCaching(); // Enable caching
```

```dart
RDB.disableCaching(); // Disable caching
```

### Using the emulator
You can use the Realtime Database emulator for local development and testing using `RDB.useEmulator()`:
When using the emulator, you need to call this function after initializing Firestorm's RDB object
and before calling any other functions.

```dart
RDB.useEmulator("localhost", 9000); // Use the Realtime Database emulator
```

### Shutting down the Realtime Database instance
You can shut down the Realtime Database instance using `RDB.shutdown()`:
```dart
await RDB.shutdown(); // Shut down the RDB instance
```

> [!NOTE]
> This is useful for cleaning up resources when your app is closed or when you no longer need to use the Realtime Database.
> It is recommended to call this method when your app is closed or when you no longer need to use Realtime Database.
> This will close all open connections and release resources used by the Realtime Database.

> [!WARNING]
> You can also use this method to reset the Realtime Database instance and start fresh, by recalling `RDB.init()`.
> You will not be able to use any Realtime Database methods after calling `RDB.shutdown()` until you call `RDB.init()` again.

[Back to contents](#contents)

---

## Using the Realtime Database API
For total control over the Realtime Database for other types of operations, you can use the traditional Firestore API methods directly by using the `RDB.instance` property.
```dart
FirebaseFirestore firestore = RDB.instance;
//TODO - Use the Firestore instance as needed
```

[Back to contents](#contents)