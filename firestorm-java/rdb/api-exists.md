# Check if object exists

### Synchronous

With class and ID:

```java
Boolean exists = RDB.exists(MyClass.class, "id").now();
```

```java
Boolean exists = RDB.exists(MyClass.class, "id").waitFor(1, TimeUnit.MINUTES);
```

With object:

```java
Boolean exists = RDB.exists(object).now();
```

### Asynchronous

With class and ID:

```java
RDB.exists(MyClass.class, "id").then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

With object:

```java
RDB.exists(object).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```