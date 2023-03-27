# List objects of type

### Synchronous

With limit (e.g., 10 items only):

```java
List<MyClass> items = FS.list(MyClass.class, 10).now();
```

```java
List<MyClass> items = FS.list(MyClass.class, 10).now(error -> {
    //TODO
});
```

```java
List<MyClass> items = FS.list(MyClass.class, 10).waitFor(1, TimeUnit.MINUTES);
```

Without limit:

```java
List<MyClass> items = FS.list(MyClass.class).now();
```

```java
List<MyClass> items = FS.list(MyClass.class).now(error -> {
    //TODO
});
```

```java
List<MyClass> items = FS.list(MyClass.class).waitFor(1, TimeUnit.MINUTES);
```

### Asynchronous

With limit (e.g., 10 items only):

```java
FS.list(MyClass.class, 10).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

Without limit:
```java
FS.list(MyClass.class).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```

---