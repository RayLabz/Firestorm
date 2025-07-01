# Firestorm - Firestore API Guide 🔥

This guide shows examples on how to use Firestorm's Firestore API.
It covers basic operations such as creating, reading, updating, and deleting documents, as well as more advanced features 
like querying, batches, transactions, and more.

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
- [Transactions](#transactions)
- [Batch Operations](#batch-operations)
- [Real-time Listeners](#real-time-listeners)
- [Subcollections](#subcollections)
- [Pagination](#pagination)
- [References](#references)
- [Configuration options](#configuration-options)
  - [Caching](#caching)
  - [Online/Offline persistence](#onlineoffline-persistence)
  - [Using the emulator](#using-the-emulator)
  - [Shutting down the Firestore instance](#shutting-down-the-firestore-instance)
- [Using the Firestore API](#using-the-firestore-api)
---

## Initialization
To use the Firestorm Firestore API, you need to initialize it first using `FS.init()`.
This registers your classes for serialization/deserialization and provides static safety checks.
```dart
await FS.init();
```

> [!NOTE]
> This operation is asynchronous and should be called before using any other Firestorm API methods.
> It is recommended to call this in the `main()` function of your app, before running any other code.

Alternatively, you can initialize Firestorm with a custom `FirebaseOptions` configuration:
```dart
await FS.init(options);
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
await FS.create.one(person);
```

#### Multiple objects (list) using `create.many()`:
```dart
await FS.create.many(people);
```

[Back to contents](#contents)

---

### Getting objects
#### Single object using `get.one()`:
```dart
Person person = await FS.get.one<Person>("person-id");
```

#### Multiple objects (list) using `get.many()`:
```dart
List<Person> people = await FS.get.many<Person>(["person-id-1", "person-id-2"]);
```

[Back to contents](#contents)

---

### Updating objects
#### Single object using `update.one()`:
```dart
await FS.update.one(person);
```

#### Multiple objects (list) using `update.many()`:
```dart
await FS.update.many(people);
```

[Back to contents](#contents)

---

### Deleting objects
#### Single object using `delete.one()`:
```dart
await FS.delete.one(person);
```

#### Single object using its ID with `delete.oneWithID()`:
```dart
await FS.delete.oneWithID(Person, "person-id");
```

#### Multiple objects (list) using `delete.many()`:
```dart
await FS.delete.many(people);
```

#### Many objects using their ID with `delete.manyWithID()`:
```dart
await FS.delete.manyWithID(Person, ["person-id-1", "person-id-2"]);
```

#### All objects of a type using `delete.all()`:
```dart
await FS.delete.all(Person, iAmSure: true);
```

> [!NOTE]
> The `iAmSure: true` parameter is required to prevent accidental deletion of all objects of a type.

[Back to contents](#contents)

---

### Listing objects

#### Up to 10 objects of a type using `list.ofClass()`:
```dart
List<Person> people = await FS.list.ofClass<Person>();
```

#### With optional custom limit:
```dart
List<Person> people = await FS.list.ofClass<Person>(limit: 5);
```

#### All objects of a type using `list.allOfClass()`:
```dart
List<Person> people = await FS.list.allOfClass<Person>();
```

[Back to contents](#contents)

---

### Checking existence of objects

#### Single object using `exists.one()`:
```dart
bool exists = await FS.exists.one<Person>(person);
```

#### Single object using its ID `exists.oneWithID()`:
```dart
bool exists = await FS.exists.oneWithID(Person, "person-id");
```

[Back to contents](#contents)

---

## Queries & Filtering

### Query examples

#### Basic query to filter objects by a field value:

```dart
//Query:
FSQueryResult<Person> result = await FS.list.filter<Person>(Person)
        .whereGreaterThanOrEqualTo("age", 18)
        .fetch();

//Obtain result:
List<Person> filteredPeople = result.items;
```

#### Query with multiple conditions:

```dart
//Query:
FSQueryResult<Person> result = await FS.list.filter<Person>(Person)
        .whereEqualTo("city", "New York")
        .orderBy("name", descending: true)
        .limit(10)
        .fetch();
//Obtain result:
List<Person> filteredPeople = result.items;
```

> [!NOTE]
> You can chain multiple query functions to build complex queries.

> [!NOTE]
> Sorting/ordering queries are only supported for fields that are indexed in Firestore.
> If you encounter an error about missing indexes, you can create the required index in the Firestore console.
> You can find more information about creating indexes in the [Firestore documentation](https://firebase.google.com/docs/firestore/query-data/indexing).

> [!NOTE]
> Sorting functions such as `orderBy` must be used before `limit`, `startAt`, `endAt`, etc.' and after any `where` clauses.

### Query functions reference
| Function | Description |
| --- | --- |
| `whereEqualTo(field, value)` | Filters documents where the specified field is equal to the given value. |
| `whereNotEqualTo(field, value)` | Filters documents where the specified field is not equal to the given value. |
| `whereLessThan(field, value)` | Filters documents where the specified field is less than the given value. |
| `whereLessThanOrEqualTo(field, value)` | Filters documents where the specified field is less than or equal to the given value. |
| `whereGreaterThan(field, value)` | Filters documents where the specified field is greater than the given value. |
| `whereGreaterThanOrEqualTo(field, value)` | Filters documents where the specified field is greater than or equal to the given value. |
| `whereArrayContains(field, value)` | Filters documents where the specified field is an array containing the given value. |
| `whereArrayContainsAny(field, values)` | Filters documents where the specified field is an array containing any of the given values. |
| `whereIn(field, values)` | Filters documents where the specified field is equal to any of the given values. |
| `whereNotIn(field, values)` | Filters documents where the specified field is not equal to any of the given values. |
| `orderBy(field, {descending: false})` | Orders the results by the specified field, optionally in descending order. |
| `limit(int limit)` | Limits the number of results returned by the query. |
| `startAt([values])` | Starts the query at the specified values. |
| `startAfter([values])` | Starts the query after the specified values. |
| `endAt([values])` | Ends the query at the specified values. |
| `endBefore([values])` | Ends the query before the specified values. |
| `stream()` | Returns a stream of results that updates in real-time as the data changes. |
| `fetch()` | Executes the query and returns the results as a list. |

[Back to contents](#contents)

---

## Transactions

#### Simple transaction example
You can execute a transaction using the `FS.transaction.run()` method.

```dart
  FS.transaction.run((transaction) async {
    Person? p =  await transaction.get.one<Person>(person.id);
    if (p == null) {
      print("Person not found"); //TODO - Error handling logic?
      return;
    }
    
    p.age = 20;
    transaction.update.one(p);
  });
```

> [!NOTE]
> Transactions are useful for performing multiple read and write operations atomically.
> If any operation fails, the entire transaction is rolled back.
> You can use transactions to ensure data consistency, especially when multiple users might be updating the same data concurrently.

> [!NOTE]
> In a transaction, you can use the `transaction.get.one()` method to read a document, and then use `transaction.update.one()` or `transaction.create.one()` to modify/create. 
> You can also use `transaction.delete.one()` to delete a document.
> You cannot use the regular `FS.get.one()` or `FS.update.one()` methods inside a transaction.

#### Transaction with additional parameters:
You can also pass additional parameters to the transaction, such as `maxAttempts` to control the number of retry
attempts in case of contention, and `timeout` to control the time to wait for it to be completed.

```dart
  FS.transaction.run((transaction) async {
    Person? p =  await transaction.get.one<Person>(person.id);
    if (p == null) {
    print("Person not found"); //TODO - Error handling logic?
    return;
    }
    
    p.age = 20;
    transaction.update.one(p);
  }, maxAttempts: 3, timeout: Duration(seconds: 5)).then((value) {
    print("Transaction completed successfully"); //TODO - Success handling logic?
  }).catchError((error) {
    print("Transaction failed: $error"); //TODO - Error handling logic?
  });
```

[Back to contents](#contents)

---

## Batch Operations
You can execute batch operations using the `FS.batch.run()` method.
This allows you to perform multiple create, update, and delete operations in a single batch.
Batch operations are useful for optimizing performance and reducing the number of network requests.

```dart
FS.batch.run((batch) async {
  await batch.create.one(purchaseRecord);
  await batch.update.one(customer);
  await batch.delete.one(bookRecord);
});
```

[Back to contents](#contents)

---

## Real-time Listeners

### Listen to a single object:
```dart
FS.listen.toObject<Person>(
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
FS.listen.toObjects<Person>(
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
FS.listen.toID<Person>(
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
FS.listen.toIDs<ComputingStudent>(
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

[Back to contents](#contents)

---

## Subcollections

You can access subcollections using the `subcollection` attribute, which is available for most calls:

### Example: Creating an object in a subcollection
```dart
await FS.create.one(person, subcollection: "friends");
```

### Example: Getting an object from a subcollection
```dart
Person person = await FS.get.one<Person>("person-id", subcollection: "friends");
```

**Firestorm offers a way to access subcollections in a variety of other methods.**

> [!NOTE]
> Subcollections are useful for organizing related data as part of a specific class.
> They can be used as an alternative to indexing or filtering data, to provide a quicker 
> way to access related objects without needing to perform complex queries.

[Back to contents](#contents)

---

## Pagination
You can paginate results using the `FS.paginate()` method:
```dart
//Create a paginator:
FSPaginator<Person> paginator = FS.paginate<Person>();

// Fetch the first page of results and print them:
FSQueryResult<Person> page1 = await paginator.next();
print("Page 1:");
page1.items.forEach((person) {
  print("Person ID: ${person.id}, Name: ${person.name}");
});

// Fetch the next page of results and print them:
FSQueryResult<Person> page2 = await paginator.next();
print("Page 2:");
page2.items.forEach((person) {
  print("Person ID: ${person.id}, Name: ${person.name}");
});

//etc...
```

You can use a custom number of results to retrieve for pagination:
```dart
FSPaginator<Person> paginator = FS.paginate<Person>(limit: 20);
//...
```

You can also set the starting point for pagination using a document ID:
```dart
FSPaginator<Person> paginator = FS.paginate<Person>(lastDocumentID: "some-document-id");
//...
```

[Back to contents](#contents)

---

## References

You can create references to objects or entire classes using the `FS.reference` method.
This returns a traditional Firestore reference that can be used to access the object or class.

### Reference to single document with class and ID
```dart
DocumentReference<Map<String, dynamic>> reference = FS.reference.documentFromID(Person, "person-id");
```

### Reference to single document with object
```dart
DocumentReference<Map<String, dynamic>> reference = FS.reference.documentFromObject(person);
```

### Reference to a single document with path
```dart
DocumentReference<Map<String, dynamic>> reference = FS.reference.documentFromPath("Person/person-id");
```

### Reference to a collection with class
```dart
CollectionReference reference = FS.reference.collection(ComputingStudent);
```

[Back to contents](#contents)

---

## Configuration options
Firestorm provides several configuration options to customize its behavior.

### Caching
You can enable or disable caching for Firestore operations using `FS.enableCaching()` or `FS.disableCaching()`:
```dart
FS.enableCaching(); // Enable caching
```

```dart
FS.disableCaching(); // Disable caching
```

### Online/Offline persistence
You can enable or disable online/offline persistence using `FS.enableNetwork()` or `FS.disableNetwork()`:
```dart
FS.enableNetwork(); // Enable online/offline persistence
```

```dart
FS.disableNetwork(); // Disable online/offline persistence
```

### Using the emulator
You can use the Firestore emulator for local development and testing using `FS.useEmulator()`:
When using the emulator, you need to call this function after initializing Firestorm's FS object 
and before calling any other functions.

```dart
FS.useEmulator("localhost", 8080); // Use the emulator at localhost:8080
```

### Shutting down the Firestore instance
You can shut down the Firestore instance using `FS.shutdown()`:
```dart
await FS.shutdown(); // Shut down the Firestore instance
```

> [!NOTE]
> This is useful for cleaning up resources when your app is closed or when you no longer need to use Firestore.
> It is recommended to call this method when your app is closed or when you no longer need to use Firestore.
> This will close all open connections and release resources used by Firestore.

> [!WARNING]
> You can also use this method to reset the Firestore instance and start fresh, by recalling `FS.init()`.
> You will not be able to use any Firestore methods after calling `FS.shutdown()` until you call `FS.init()` again.

[Back to contents](#contents)

---

## Using the Firestore API
For total control over the Firestore for other types of operations, you can use the traditional Firestore API methods directly by using the `FS.instance` property.
```dart
FirebaseFirestore firestore = FS.instance;
//TODO - Use the Firestore instance as needed
```

[Back to contents](#contents)