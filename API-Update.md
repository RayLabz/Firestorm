# Update single object

### Synchronous

```java 
FS.update(object).now();
```

```java
FS.update(object).now(error -> {
    //TODO
});
```

```java
FS.update(object).waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``WriteResult``.

### Asynchronous

```java
FS.update(object).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---

# Update multiple objects

### Synchronous

With ``java.util.List``:

```java
FS.update(objects).now();
```

```java
FS.update(objects).now(error -> {
    //TODO
});
```

```java
FS.update(objects).waitFor(1, TimeUnit.MINUTES);
```

With varargs:

```java
FS.update(object1, object2).now();
```

```java
FS.update(object1, object2).now(error -> {
    //TODO
});
```

```java
FS.update(object1, object2).waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``List<WriteResult>``.

### Asynchronous

With ``java.util.List``:

```java
FS.update(objects).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With varargs:

```java
FS.update(object1, object2).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---