# Firestorm: Firestore object listeners

An object listener listens for updates to one document.

## Attaching listeners

Use `FS.attachListener()` to attach a listener to an object.

### Using ID reference

```java
ListenerRegistration listener = FS.attachListener(Person.class, "id", new RealtimeUpdateCallback<>() {
    @Override
    public void onUpdate(Person data) {
        System.out.println(data); //TODO - Handle onUpdate()
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(t.getMessage()); //TODO - handle onError()
    }
});
```

### Using an existing object

```java
ListenerRegistration listener = FS.attachListener(object, new RealtimeUpdateCallback<>() {
    @Override
    public void onUpdate(Person data) {
        System.out.println(data); //TODO - Handle onUpdate()
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(t.getMessage()); //TODO - handle onError()
    }
});
```

## Detaching a listener

You can remove a listener by storing a reference to the listener and then calling `FS.detachListener()`.

```java
FS.detachListener(listener);
```