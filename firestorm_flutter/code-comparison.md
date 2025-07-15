# Firestorm vs Native APIs

This file showcases the use of the Firestorm API in comparison with the native API for Firestore and Realtime database.

## Creating an item

Firestore:

```dart
await fs.collection('Person')
  .doc(person.id)
  .set({
    id: person.id,
    name: person.name,
    age: person.age
  });
```

Realtime database:
```dart
await db.ref()
  .child("Person")
  .child(person.id)
  .set({
    id: person.id,
    name: person.name,
    age: person.age
  });
```

Firestorm:
```dart
await FS.create.one(person); //Firestore
//or
await RDB.create.one(person); //Realtime database
```

---

## Retrieving an item

Firestore:

```dart
var snapshot = await fs.collection("Person")
  .doc(person.id)
  .get();

Person? p;

if (snapshot.exists) {
  p = Person(
    snapshot.get("id") as String,
    snapshot.get("name") as String,
    snapshot.get("age") as int
  );
}
```

Realtime database:

```dart
var snapshot = await db.ref("Person")
  .child(person.id)
  .get();

Person? p;

if (snapshot.exists) {
  final data = snapshot.value as Map<dynamic, dynamic>;
  p = Person(
    data["id"] as String,
    data["name"] as String,
    data["age"] as int,
  );
}
```

Firestorm:

```dart
Person? p = await FS.get.one("person-id"); //Firestore
//or
Person? p = await RDB.get.one("person-id"); //Realtime database
```

---

## Updating an item

Firestore:

