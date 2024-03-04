# Firestorm: Firestore pagination

## Synchronous

```java
String lastDocumentID = null; //Stores the ID of the last document received.

//Get the next 10 items:
FSQueryResult<Person> result = FSPaginator.next(Person.class, lastDocumentID, 10)
        .fetch()
        .now();

ArrayList<Person> items = result.getItems(); //Get items
lastDocumentID = result.getLastDocumentID(); //Replace lastDocumentID with the last from the current query.
```

## Asynchronous

```java
AtomicReference<String> lastDocumentID = null; //Stores the ID of the last document received.
//Note: Must use AtomicReference to use variable in lamba expression.

//Get the next 10 items:
FSPaginator.next(Person.class, lastDocumentID.get(), 10)
        .fetch()
        .then(result1 -> {
            ArrayList<Person> items = result1.getItems(); //Get items
            lastDocumentID.set(result1.getLastDocumentID()); //Replace lastDocumentID with the last from the current query.
        });
```