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

