# Firestorm: Firestore filterable listeners

Filterable listeners only listen for updates to a specific set of documents on Firestore rather than the whole class.

## Attaching listeners

Assuming you have a filterable which you can use to limit the number of items retrieved, you can use `FS.attachFilterableListener()` 
to attach a listener to the collection of documents referenced by the filterable.

```java
//Create filterable:
FSFilterable<Person> filterable = FS.filter(Person.class)
    .whereEqualTo("age", 30);

//Create listener:
ListenerRegistration listener = FS.attachFilterableListener(filterable, new RealtimeUpdateCallback<>() {
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