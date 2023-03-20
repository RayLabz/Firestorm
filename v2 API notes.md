# API:

## Create

### Create single object

#### Synchronous

```java 
FS.create(object).now()
```

```java
FS.create(object).now(error -> {
    //TODO
});
```

```java
FS.create(object).waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``WriteResult``.

#### Asynchronous

```java
FS.create(object).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

### Get single object

#### Synchronous

```java
MyClass object = FS.get(MyClass.class, "documentID").now();
```

```java
MyClass object = FS.get(MyClass.class, "documentID").waitFor(1, TimeUnit.MINUTES);
```

#### Asynchronous

```java
FS.get(MyClass.class, "documentID").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

### Get many objects

#### Synchronous

With varargs:
```java
List<MyClass> items = FS.getMany(MyClass.class, "id_0", "id_1").now();
```

With ``java.util.List``:
```java
List<MyClass> items = FS.getMany(MyClass.class, idsList).now();
```

waitFor() with varargs:
```java
List<MyClass> items = FS.getMany(MyClass.class, "id_0", "id_1").waitFor(1, TimeUnit.MINUTES);
```

waitFor() with ``java.util.List``:
```java
List<MyClass> items = FS.getMany(MyClass.class, idsList).waitFor(1, TimeUnit.MINUTES);
```

#### Asynchronous

With varargs:
```java
FS.getMany(MyClass.class, "id_0", "id_1").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With ``java.util.List``:
```java
FS.getMany(MyClass.class, idsList).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

### Check if object exists

#### Synchronous

```java
Boolean exists = FS.exists(MyClass.class, "id").now();
```

```java
Boolean exists = FS.exists(Student.class, "id_0").waitFor(1, TimeUnit.MINUTES);
```

#### Asynchronous

```java
FS.exists(Student.class, "id_0").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

