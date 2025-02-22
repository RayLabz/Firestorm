# Firestorm: Firestore class listeners

Class listeners listen for updates to all documents of a class.

## Attaching listeners

Use `FS.attachClassListener()` to attach a listener to a class.

```java
ListenerRegistration listener = FS.attachClassListener(Person.class, new RealtimeUpdateCallback<>() {
    @Override
    public void onUpdate(List<FSObjectChange<Person>> data) {
        System.out.println(data); //TODO - Handle onUpdate()
    }

    @Override
    public void onError(Throwable t) {
        System.err.println(t.getMessage()); //TODO - Handle onError()
    }
});
```

## Detaching a listener

You can remove a listener by storing a reference to the listener and then calling `FS.detachListener()`.

```java
FS.detachListener(listener);
```