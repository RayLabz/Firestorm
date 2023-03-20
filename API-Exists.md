# Check if object exists

### Synchronous

```java
Boolean exists = FS.exists(MyClass.class, "id").now();
```

```java
Boolean exists = FS.exists(Student.class, "id_0").waitFor(1, TimeUnit.MINUTES);
```

### Asynchronous

```java
FS.exists(Student.class, "id_0").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```