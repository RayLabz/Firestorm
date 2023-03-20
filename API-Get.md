# Get single object

### Synchronous

```java
MyClass object = FS.get(MyClass.class, "documentID").now();
```

```java
MyClass object = FS.get(MyClass.class, "documentID").now(error -> {
    //TODO
});
```

```java
MyClass object = FS.get(MyClass.class, "documentID").waitFor(1, TimeUnit.MINUTES);
```

### Asynchronous

```java
FS.get(MyClass.class, "documentID").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---

# Get many objects

### Synchronous


With ``java.util.List``:

```java
List<MyClass> items = FS.getMany(MyClass.class, idsList).now();
```

```java
List<MyClass> items = FS.getMany(MyClass.class, idsList).now(error -> {
    //TODO
});
```

```java
List<MyClass> items = FS.getMany(MyClass.class, idsList).waitFor(1, TimeUnit.MINUTES);
```

With varargs:

```java
List<MyClass> items = FS.getMany(MyClass.class, "id_0", "id_1").now();
```

```java
List<MyClass> items = FS.getMany(MyClass.class, "id_0", "id_1").now(error -> {
    //TODO
});
```

```java
List<MyClass> items = FS.getMany(MyClass.class, "id_0", "id_1").waitFor(1, TimeUnit.MINUTES);
```

### Asynchronous

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