# Firestorm: RDB class listeners

Class listeners listen for updates to all objects of a class.

## Attaching listeners

Use `RDB.attachClassListener()` to attach a listener to a class.

```java
ChildEventListener listener = RDB.attachClassListener(MyClass.class, new ChildEventListener() {
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