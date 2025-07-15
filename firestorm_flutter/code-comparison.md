# Firestorm vs Native APIs - Code comparison

This file demonstrates the use of the Firestorm API in comparison with the native API for Firestore and Realtime database.
The Firestorm API is:

1. Significantly more concise
2. Easier to use
3. Learnable
4. Logically tied to the operations being made

This can allow you to focus on their application logic rather than the intricacies of database
operations.

---

## Creating an item

Firestore ❌:

```dart
await fs.collection('Person')
  .doc(person.id)
  .set({
    id: person.id,
    name: person.name,
    age: person.age
  });
```

Realtime database ❌:
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

Firestorm ✅:
```dart
await FS.create.one(person); //Firestore
//or
await RDB.create.one(person); //Realtime database
```

---

## Retrieving an item

Firestore ❌:

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

Realtime database ❌:

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

Firestorm ✅:

```dart
Person? p = await FS.get.one("person-id"); //Firestore
//or
Person? p = await RDB.get.one("person-id"); //Realtime database
```

> [!NOTE] Operations for updating and deleting items feature similar code, using `update` and `delete` methods respectively.

---

## Querying items

Firestore ❌:

```dart
var snapshot = await fs.collection("Person")
  .where("age", isGreaterThan: 18)
  .get();

List<Person> people = snapshot.docs.map((doc) {
  return Person(
    doc.get("id") as String,
    doc.get("name") as String,
    doc.get("age") as int
  );
}).toList();
```

Realtime database ❌:

```dart
var snapshot = await db.ref("Person")
  .orderByChild("age")
  .startAt(18)
  .get();

List<Person> people = [];
for (var child in snapshot.children) {
  final data = child.value as Map<dynamic, dynamic>;
  people.add(Person(
    data["id"] as String,
    data["name"] as String,
    data["age"] as int,
  ));
}
```

Firestorm (Firestore) ✅:

```dart
FSQueryResult<Person> result = await FS.list.filter<Person>(Person)
    .whereGreaterThan('age', 18)
    .fetch();

List<Person> people = result.items;
```

Firestorm (Realtime database) ✅:

```dart
RDBQueryResult<Person> result = await RDB.list.filter<Person>(Person)
  .startAt(18, field: "age")
  .fetch();
  
List<Person> people = result.items;
```

---

## Listening to changes

Firestore ❌

```dart
fs.collection("Person").snapshots().listen((snapshot) {
    List<Person> people = snapshot.docs.map((doc) {
      return Person(
        doc.get("id") as String,
        doc.get("name") as String,
        doc.get("age") as int
      );
    }).toList();
    // Handle the updated list of people
});
```

Realtime database ❌:

```dart
db.ref("Person").onValue.listen((event) {
  var snapshot = event.snapshot;
  List<Person> people = [];
  for (var child in snapshot.children) {
    final data = child.value as Map<dynamic, dynamic>;
    people.add(Person(
      data["id"] as String,
      data["name"] as String,
      data["age"] as int,
    ));
  }
  // Handle the updated list of people
});
```

Firestorm ✅:

```dart
StreamSubscription subscription = await FS.listen.toObject<Person>(
  person,
  onCreate: (p) {
    // React to student created
  },
  onChange: (p) {
    // React to student updated
  },
  onDelete: () {
    // React to student deleted
  },
);
```

---

## Transactions

Firestore ❌:

```dart
await fs.runTransaction((transaction) async {
  var docRef = fs.collection("Person").doc(person.id);
  var snapshot = await transaction.get(docRef);

  if (snapshot.exists) {
    Person p = Person(
      snapshot.get("id") as String,
      snapshot.get("name") as String,
      snapshot.get("age") as int
    );
    // Modify p as needed
    transaction.update(docRef, {
      "name": p.name,
      "age": p.age
    });
  }
});
```

Realtime database ❌:

```dart
await db.ref("Person").runTransaction((mutableData) async {
  var personData = mutableData.value as Map<dynamic, dynamic>?;

  if (personData != null) {
    Person p = Person(
      personData["id"] as String,
      personData["name"] as String,
      personData["age"] as int
    );
    // Modify p as needed
    mutableData.value = {
      "name": p.name,
      "age": p.age
    };
  }
});
```

Firestorm ✅:

```dart
await FS.transaction.run((transaction) async {
  Person? p = await transaction.get.one(Person, "person-id");
  // Modify p as needed
  transaction.update.one(p);
});
```





