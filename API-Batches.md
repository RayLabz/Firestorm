# Batch operations

## Synchronous

```java
FS.runBatch(new FirestormBatch() {
    @Override
    public void execute() {
        create(object1);
        create(object2);
        create(object3);
        delete(object2);
    }
}).now();
```

```java
FS.runBatch(new FirestormBatch() {
    @Override
    public void execute() {
        create(object1);
        create(object2);
        create(object3);
        delete(object2);
    }
}).waitFor(1, TimeUnit.MINUTES);
```

**Note**:

``now()`` and ``waitFor()`` return a ``List<WriteResult>``.

## Asynchronous

```java
FS.runBatch(new FirestormBatch() {
    @Override
    public void execute() {
        create(object1);
        create(object2);
        create(object3);
        delete(object2);
    }
}).then(result -> {
    //TODO - Success
}).onError(error -> {
    //TODO - Error
}).run();
```