# Delete single object

### Synchronous

With objects:

```java 
FS.delete(object).now();
```

```java
FS.delete(object).now(error -> {
    //TODO
});
```

```java
FS.delete(object).waitFor(1, TimeUnit.MINUTES);
```

With class and document/object ID:

```java 
FS.delete(MyClass.class, "id").now();
```

```java
FS.delete(MyClass.class, "id").now(error -> {
    //TODO
});
```

```java
FS.delete(MyClass.class, "id").waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``WriteResult``.

### Asynchronous

With objects:

```java
FS.delete(object).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With class and document/object ID:

```java
FS.delete(MyClass.class, "id").then(result -> {
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
FS.delete(objects).now();
```

```java
FS.delete(objects).now(error -> {
    //TODO
});
```

```java
FS.delete(objects).waitFor(1, TimeUnit.MINUTES);
```

With varargs:

```java
FS.delete(object1, object2).now();
```

```java
FS.delete(object1, object2).now(error -> {
    //TODO
});
```

```java
FS.delete(object1, object2).waitFor(1, TimeUnit.MINUTES);
```

With ID varargs:

```java
FS.delete(MyClass.class, "id1", "id2").now();
```

```java
FS.delete(MyClass.class, "id1", "id2").now(error -> {
    //TODO
});
```

```java
FS.delete(MyClass.class, "id1", "id2").waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``List<WriteResult>``.

### Asynchronous

With ``java.util.List``:

```java
FS.delete(objects).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With varargs:

```java
FS.delete(object1, object2).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With ID varargs:

```java
FS.delete(MyClass.class, "id1", "id2").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---