# Delete single object

### Synchronous

With objects:

```java 
RDB.delete(object).now();
```

```java
RDB.delete(object).now(error -> {
    //TODO
});
```

```java
RDB.delete(object).waitFor(1, TimeUnit.MINUTES);
```

With class and document/object ID:

```java 
RDB.delete(MyClass.class, "id").now();
```

```java
RDB.delete(MyClass.class, "id").now(error -> {
    //TODO
});
```

```java
RDB.delete(MyClass.class, "id").waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``WriteResult``.

### Asynchronous

With objects:

```java
RDB.delete(object).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With class and document/object ID:

```java
RDB.delete(MyClass.class, "id").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---

# Delete multiple objects

### Synchronous

With ``java.util.List``:

```java
RDB.delete(objects).now();
```

```java
RDB.delete(objects).now(error -> {
    //TODO
});
```

```java
RDB.delete(objects).waitFor(1, TimeUnit.MINUTES);
```

With varargs:

```java
RDB.delete(object1, object2).now();
```

```java
RDB.delete(object1, object2).now(error -> {
    //TODO
});
```

```java
RDB.delete(object1, object2).waitFor(1, TimeUnit.MINUTES);
```

With ID varargs:

```java
RDB.delete(MyClass.class, "id1", "id2").now();
```

```java
RDB.delete(MyClass.class, "id1", "id2").now(error -> {
    //TODO
});
```

```java
RDB.delete(MyClass.class, "id1", "id2").waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``List<WriteResult>``.

### Asynchronous

With ``java.util.List``:

```java
RDB.delete(objects).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With varargs:

```java
RDB.delete(object1, object2).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With ID varargs:

```java
RDB.delete(MyClass.class, "id1", "id2").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---

## Delete all objects of type

### Synchronous

```java
RDB.deleteType(MyClass.class).now();
```

```java
RDB.deleteType(MyClass.class).waitFor(1, TimeUnit.MINUTES);
```

### Asynchronous

```java
RDB.deleteType(MyClass.class).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```