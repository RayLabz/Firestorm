# Create single object

### Synchronous

```java 
RDB.set(object).now();
```

```java
RDB.set(object).now(error -> {
    //TODO
});
```

```java
RDB.set(object).waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``WriteResult``.

### Asynchronous

```java
RDB.set(object).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---

# Create multiple objects

### Synchronous

With ``java.util.List``:

```java
RDB.set(objects).now();
```

```java
RDB.set(objects).now(error -> {
    //TODO
});
```

```java
RDB.set(objects).waitFor(1, TimeUnit.MINUTES);
```

With varargs:

```java
RDB.set(object1, object2).now();
```

```java
RDB.set(object1, object2).now(error -> {
    //TODO
});
```

```java
RDB.set(object1, object2).waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``List<WriteResult>``.

### Asynchronous

With ``java.util.List``:

```java
RDB.set(objects).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With varargs:

```java
RDB.set(object1, object2).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---