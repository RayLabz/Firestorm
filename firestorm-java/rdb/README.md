# Firestorm - Real-time database API Guide

[//]: # (## Contents)

[//]: # ()
[//]: # (* [Basic &#40;CRUD&#41; operations]&#40;#basic-operations&#41;)

[//]: # (* [Queries & filtering/sorting]&#40;#queries--filteringsorting&#41;)

[//]: # (* [Transactions & batch operations]&#40;#transactions--batch-operations&#41;)

[//]: # (* [Pagination]&#40;#pagination&#41;)

[//]: # (* [Listeners]&#40;#listeners&#41;)

[//]: # (---)

## Offline persistence

The real-time database supports offline persistence of data (local caching). This can be enabled by running
`RDB.enableOffline()`. You can also disable offline persistence using `RDB.disableOffline()`.

## Basic operations

Firestorm supports basic (CRUD) operations on data.

[Set](api-set.md) |
[Set multiple](api-set.md#create-multiple-objects) |
[Get](api-get.md) |
[Exists](api-exists.md) |
[Delete](api-delete.md) |
[Delete all of type](api-delete.md#delete-all-objects-of-type) |
[List objects of type](api-list.md)

## Queries, filtering, and sorting

[Synchronous queries](queries.md#synchronous) |
[Asynchronous queries](queries.md#asynchronous) |
[Query function reference](queries.md#query-function-reference) 

## Listeners

Firestorm supports real-time updates on database objects by using object, class, and filter listeners.

[Object listeners](listeners-objects.md) |
[Class listeners](listeners-classes.md) |
[Filterable listeners](listeners-filterables.md)

## Utilities

### Real-time database instance

Firestorm provides the full flexibility of the Real-time database original API by allowing access to the instance object
using `RDB.getInstance()`. This can allow full access to the RDB API without any limitations.