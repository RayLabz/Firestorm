# Firestorm: RDB object listeners

An object listener listens for updates to one document.

## Attaching listeners

Use `RDB.attachListener()` to attach a listener to an object.

### Using ID reference
```java
ValueEventListener listener = RDB.attachListener(MyClass.class, "id", new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //TODO - Handle on data change.
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //TODO - Handle on cancelled.
    }
});
```


### Using an existing object

```java
ValueEventListener listener = RDB.attachListener(object, new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        //TODO - Handle on data change.
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        //TODO - Handle on cancelled.
    }
});
```

## Detaching a listener

You can remove a listener by storing a reference to the listener and then calling `RDB.detachListener()`.

```java
RDB.detachListener(listener);
```