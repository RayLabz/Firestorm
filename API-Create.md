# Create single object

### Synchronous

```java 
FS.create(object).now();
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

### Asynchronous

```java
FS.create(object).then(result -> {
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
FS.create(objects).now();
```

```java
FS.create(objects).now(error -> {
    //TODO
});
```

```java
FS.create(objects).waitFor(1, TimeUnit.MINUTES);
```

With varargs:

```java
FS.create(object1, object2).now();
```

```java
FS.create(object1, object2).now(error -> {
    //TODO
});
```

```java
FS.create(object1, object2).waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``List<WriteResult>``.

---