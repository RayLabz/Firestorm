# Transactions

* ``create(object)``
* ``update(object)``
* ``delete(object)``
* ``delete(aClass, objectID)``
* ``get(aClass, objectID)``
* ``list(aClass, limit)``
* ``list(aClass)``
* ``filter(aClass)``

**Note:** Transaction read operations must always precede write operations based on Firestore policies.

## Synchronous

```java
FS.runTransaction(new FirestormTransaction() {
    @Override
    public void execute() {
        list(MyClass.class);
        get(MyClass.class, "id1");
        create(object);
    }
}).now();
```

```java
FS.runTransaction(new FirestormTransaction() {
    @Override
    public void execute() {
        list(MyClass.class);
        get(MyClass.class, "id1");
        create(object);
    }
}).waitFor(1, TimeUnit.MINUTES);
```

## Asynchronous

```java
FS.runTransaction(new FirestormTransaction() {
    @Override
    public void execute() {
        list(MyClass.class);
        get(MyClass.class, "id1");
        create(object);
    }
}).then(result -> {
    //TODO - Success (no result)
}).onError(error -> {
    //TODO - Error
}).run();
```