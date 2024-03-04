# Firestorm: RDB filterable listeners

Filterable listeners only listen for updates to a specific set of documents on RDB rather than the whole class.

## Attaching listeners

Assuming you have a filterable which you can use to limit the number of items retrieved, you can use `FS.attachFilterableListener()` 
to attach a listener to the collection of documents referenced by the filterable.

```java
//Create filterable:
OrderedRDBFilterable<MyClass> filterable = RDB.filter(MyClass.class)
        .whereEqualTo("age", 30);

//Create listener:
RDB.attachFilterableListener(filterable, new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        //TODO - Implement
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        //TODO - Implement
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        //TODO - Implement
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        //TODO - Implement
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //TODO - Implement
    }
});
```

## Detaching a listener

You can remove a listener by storing a reference to the listener and then calling `RDB.detachListener()`.

```java
RDB.detachListener(listener);
```