# Firestorm Java 11 API Guide

---

This guide provides instructions on how to set up and use Firestorm in Java 11+.

## Contents

1. [Setting up](#setting-up)
2. [Initializing access to Firebase](#initializing-access-to-firebase)
   1. [Initializing Firestore](#initializing-firestore)
   2. [Initializing Real-time Database](#initializing-real-time-database)
3. [Creating a class](#creating-a-class)
   1. [Example](#example)
   2. [Excluding attributes](#excluding-attributes)
4. [Registering a class](#registering-a-class)
5. [Using the database APIs](#using-the-database-apis)
   1. [Firestore API](#firestore-api)
   2. [Real-time database API](#real-time-database-api)


## Setting up

### Download and use

You can install Firestorm by using Maven or downloading the latest .jar file.
**Please note that the latest version of Firestorm supports Firestore and Real-time database, but is still in alpha testing.**

#### Maven
```xml
<dependency>
  <groupId>com.raylabz</groupId>
  <artifactId>firestorm</artifactId>
  <version>2.0.0-alpha</version>
</dependency>
```

#### Java Archive (JAR)
[Download here (2.0.0-alpha)](https://repo1.maven.org/maven2/com/raylabz/firestorm/2.0.0-alpha/firestorm-2.0.0-alpha.jar)

---

## Initializing access to Firebase

A project that uses Firestorm must be connected to Firebase. This requires a _**service account**_, which can be accessed
from a file and loaded as a string. To generate new service account credentials go to the Firebase project settings > Service Accounts > Firebase Admin SDK, and then select 
'Generate new private key'. This will download a file which contains the credentials needed to access Firebase from your project.

> **Important**:
> Remember to keep the service account data confidential, as it provides full access to your firebase project!

Once you have a service account which is loaded into your code as a string, you can use the
`FirebaseUtils.initialize()` function to initialize Firebase:

```java
//Initialize Firestore:
String serviceAccountData = "..."; //Contains your service account data
try {
    FirebaseUtils.initialize(serviceAccountData);
} catch (IOException e) {
    e.printStackTrace();
    //TODO - Handle the exception gracefully.
}
```

After initializing Firebase, you must initialize each datastore you want to use.

### Initializing Firestore
If you want to use Firestore, use the `FS.init()` command:

```java
FS.init();
```

### Initializing Real-time Database

If you want to use the Real-time database, use the `RDB.init("https://<PROJECT_NAME>.firebaseio.com/")` command, where `PROJECT_NAME`
is your project's name:

```java
RDB.init("https://<PROJECT_NAME>.firebaseio.com/");
```

> You can use a combination of both Firestore and Real-time database at the same time.

> If you are using Google's App Engine, check [this guide](app-engine.md) on how to initialize Firestorm with a context listener. 

---

## Creating a class

Classes which are managed by Firestorm must meet several requirements:

* Be annotated with the `@FirestormObject` annotation.
* Have an attribute called `id`, of type `String`, or extend a class that has this field.
* Have an empty (no-parameter) constructor. The access modified does not matter (_the constructor can be set to `private` for safety_).

> The ID attribute's value indicates an object that also exists on the database. 
> The value `null` for the ID (e.g. before being written or after being deleted) indicates that this object does not exist
> on the database.

> The suggested way to provide access to data is to use private attributes with accessor (setter & getter) methods.
> If a field is marked as private and has no accessor methods, Firestorm will not be able to read or modify its data.

### Example

The following code shows an example on how to create a class that can be managed by Firestorm:

```java
@FirestormObject // <----- Annotate with @FirestormObject
public class Person {

    private String id;  // <----- Contains an String id attribute, or extends a class that does
    private String firstName;
    private String lastName;
    private int age;

    public Person(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }


    private Person() { // <----- Has a no-parameter constructor
    }

    public String getFirstName() { //<----- Read access to the variables is provided through getters.
        return firstName;
    }

    public void setFirstName(String firstName) { //<----- Write access to the variables is provided through setters.
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
```

### Excluding attributes

Attributes can be excluded by using the `@Exclude` annotation on the variable's getter method:

```java
@Exclude
public int getIgnoredField() {
    return ignoredField;
}
```

This allows the variable to still be accessed normally by the rest of the code, while being ignored by Firebase.

---

## Registering a class

Before running any operations you have to register each class that must be managed by Firestorm. 
This process is necessary as Firestorm will check that your classes meet the requirements mentioned above.

You can register your class after initializing Firestorm, by using the `Firestorm.register()` method and providing your class as a parameter:

```java
Firestorm.register(MyClass.class);
```

> Attempting to run an operation with an unregistered class will cause a `ClassRegistrationException`.

---
## Using the database APIs

Firestorm supports operations on Firestore and the Real-time database through their respective APIs. Click on the below links to learn about each API.

### Firestore API

[Firestore API Guide](fs/README.md)

### Real-time database API

[Real-time database API Guide](rdb/README.md)
