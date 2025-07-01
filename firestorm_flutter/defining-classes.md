# Firestorm for Flutter: Defining classes
This guide shows how to define classes in Firestorm for Flutter.

## Rules & Constraints
1. Classes must be annotated with `@FirestormObject()` to be recognized by the Firestorm code generator.
2. A class, or any of its superclasses, must have a field called `id` of type `String` to serve as the unique identifier for the object in the database.
3. The `id` field must be initialized in the constructor of the class.
4. A random ID can be defined using `Firestorm.randomID()`. This function generates a random ID in <a target="_blank" href="https://pub.dev/packages/uuid">uuidv8</a> format.

## Accepted data types
This section lists the Dart data types that can be used in Firestorm classes for Firestore and Realtime Database.

| Firestore 🔥         | Realtime Database ⚡  |
|----------------------|----------------------|
| String               | String               |
| int                  | int                  |
| double               | double               |
| bool                 | bool                 |
| null                 | null                 |
| DateTime             | List<dynamic>        |
| GeoPoint             | Map<String, dynamic> |
| DocumentReference    |                      |
| Uint8List            |                      |
| List<dynamic>        |                      |
| Map<String, dynamic> |                      |

> [!WARNING]
> Other data types are not supported in Firestorm classes.
> Classes with invalid data types will be ignored.

---

## Simple example
```dart
@FirestormObject()  //---> Annotate to recognize the class
class Weapon {
  
  String id;
  int damage;
  double range;
  int minLevel;
  
  Weapon(this.id, this.damage, this.range, this.minLevel);
  
}
```

## Example with inheritance
```dart
@FirestormObject()  //---> Annotate to recognize the class
class Rifle extends Weapon {
  
  int magazineSize;
  
  Rifle(String id, int damage, double range, int minLevel, this.magazineSize)
        : super(id, damage, range, minLevel);
  
}
```

## Example with composition
You can nest **Firestorm-compatible** classes within other classes as attributes. 
The nested class must also be annotated with `@FirestormObject()`.

```dart
@FirestormObject()
class Address {
  
  String street;
  String city;
  
  Address(this.street, this.city);
  
}

@FirestormObject() 
class Person {
  
  String id;
  String name;
  Address address;  //---> Nested attribute (custom class) - Composition
  
  Person(this.id, this.name, this.age, this.address);
  
}
```

## Excluding fields
To exclude fields from being stored in the database, use the `@Exclude()` annotation for a specific attribute.

```dart
@FirestormObject() 
class Person {
  
  String id;
  String name;
    
  @Exclude()  //---> Exclude this field from being stored in the database
  int age;
    
  Person(this.id, this.name, this.age);
    
}
```

## Support for enums
Firestorm supports enums as attributes in classes. Enums will be stored as their string representation in the database.
There is **no need to annotate enums**.

```dart
enum WeaponType { //---> Do NOT annoatate enums
  sword,
  bow,
  gun,
}

@FirestormObject()
class Weapon {
  
  String id;
  WeaponType type;  //---> Enum as an attribute
  
  Weapon(this.id, this.type);
  
}
```