# Firestorm - Localstore API Guide 🛖

This guide shows examples on how to use Firestorm's Localstore API.
It covers basic operations such as creating, reading, updating, and deleting documents.

## Contents

- [Basic Operations](#basic-operations)
  - [Create](#creating-objects)
  - [Get](#getting-objects)
  - [Update](#updating-objects)
  - [Delete](#deleting-objects)
  - [List](#listing-objects)
  - [Exist](#checking-existence-of-objects)
- [Subcollections](#subcollections)
- [References](#references)
- [Using the Localstore API](#using-the-localstore-api)
---

## Basic Operations

> [!CAUTION]
> A lot of operations are asynchronous and return `Future<?>`. In these cases, you may choose
> whether to `await` _(blocking)_ the operation, use then `then` or `catchError` _(non-blocking)_,
> or ignore the result entirely.

### Creating objects

#### Single object using `create.one()`:
```dart
await LS.create.one(person);
```

#### Multiple objects (list) using `create.many()`:
```dart
await LS.create.many(people);
```

[Back to contents](#contents)

---

### Getting objects
#### Single object using `get.one()`:
```dart
Person person = await LS.get.one<Person>("person-id");
```

#### Multiple objects (list) using `get.many()`:
```dart
List<Person> people = await LS.get.many<Person>(["person-id-1", "person-id-2"]);
```

[Back to contents](#contents)

---

### Updating objects
#### Single object using `update.one()`:
```dart
await LS.update.one(person);
```

#### Multiple objects (list) using `update.many()`:
```dart
await LS.update.many(people);
```

[Back to contents](#contents)

---

### Deleting objects
#### Single object using `delete.one()`:
```dart
await LS.delete.one(person);
```

#### Single object using its ID with `delete.oneWithID()`:
```dart
await LS.delete.oneWithID(Person, "person-id");
```

#### Multiple objects (list) using `delete.many()`:
```dart
await LS.delete.many(people);
```

#### Many objects using their ID with `delete.manyWithID()`:
```dart
await LS.delete.manyWithID(Person, ["person-id-1", "person-id-2"]);
```

#### All objects of a type using `delete.all()`:
```dart
await LS.delete.all(Person, iAmSure: true);
```

> [!NOTE]
> The `iAmSure: true` parameter is required to prevent accidental deletion of all objects of a type.

[Back to contents](#contents)

---

### Listing objects

#### Up to 10 objects of a type using `list.ofClass()`:
```dart
List<Person> people = await LS.list.ofClass<Person>();
```

#### With optional custom limit:
```dart
List<Person> people = await LS.list.ofClass<Person>(limit: 5);
```

#### All objects of a type using `list.allOfClass()`:
```dart
List<Person> people = await LS.list.allOfClass<Person>();
```

[Back to contents](#contents)

---

### Checking existence of objects

#### Single object using `exists.one()`:
```dart
bool exists = await LS.exists.one<Person>(person);
```

#### Single object using its ID `exists.oneWithID()`:
```dart
bool exists = await LS.exists.oneWithID(Person, "person-id");
```

[Back to contents](#contents)

---

## Subcollections

You can access subcollections using the `subcollection` attribute, which is available for most calls:

### Example: Creating an object in a subcollection
```dart
await LS.create.one(person, subcollection: "friends");
```

### Example: Getting an object from a subcollection
```dart
Person person = await LS.get.one<Person>("person-id", subcollection: "friends");
```

**Firestorm offers a way to access subcollections in a variety of other methods.**

> [!NOTE]
> Subcollections are useful for organizing related data as part of a specific class.
> They can be used as an alternative to indexing or filtering data, to provide a quicker 
> way to access related objects without needing to perform complex queries.

[Back to contents](#contents)

---

## References

You can create references to objects or entire classes using the `LS.reference` method.
This returns a traditional Firestore reference that can be used to access the object or class.

### Reference to single document with class and ID
```dart
DocumentRef reference = LS.reference.documentFromID(Person, "person-id");
```

### Reference to single document with object
```dart
DocumentRef reference = LS.reference.documentFromObject(person);
```

### Reference to a single document with path
```dart
DocumentRef reference = LS.reference.documentFromPath("Person/person-id");
```

### Reference to a collection with class
```dart
CollectionRef reference = LS.reference.collection(ComputingStudent);
```

[Back to contents](#contents)

---

## Using the Localstore API
For total control over the Localstore for other types of operations, you can use the traditional Localstore API methods directly by using the `LS.instance` property.
```dart
Localstore localstore = LS.instance;
//TODO - Use the Firestore instance as needed
```

[Back to contents](#contents)