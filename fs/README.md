# Firestorm - Firestore API Guide

## Contents

* [Basic (CRUD) operations](#basic-operations)
* [Queries & filtering/sorting](#queries--filteringsorting)
* [Transactions & batch operations](#transactions--batch-operations)
* [Pagination](#pagination)
* [Listeners](#listeners)

## Basic operations

Firestorm supports basic (CRUD) operations on data.

[Create](basic-operations.md#creating-objects) | 
[Get](basic-operations.md#retrieving-objects) |
[Update](basic-operations.md#updating-objects) |
[Delete](basic-operations.md#deleting-objects) |
[Exists](basic-operations.md#checking-if-objects-exist) |
[List objects of type](basic-operations.md#listing-objects-of-type)

[Full guide](basic-operations.md)

## Queries & filtering/sorting

Firestorm supports data queries and filtering/sorting.

[Synchronous queries](queries.md#synchronous) |
[Asynchronous queries](queries.md#asynchronous) |
[Query function reference](queries.md#query-function-reference) 


## Transactions & batch operations

Firestorm also supports transactions and batch operations.

[Synchronous transactions](API-Transactions.md#synchronous) |
[Asynchronous transactions](API-Transactions.md#asynchronous)

[Synchronous batch ops](API-Batches.md#synchronous) |
[Asynchronous batch ops](API-Batches.md#asynchronous)

## Pagination

Firestorm supports easily paginating data and retrieving multiple pages of long lists of data, one page at a time.

[Synchronous pagination](pagination.md#synchronous) |
[Asynchronous pagination](pagination.md#asynchronous)

## Listeners

Firestorm supports real-time updates on database objects by using object, class, and filter listeners.

[Object listeners](listeners-objects.md) |
[Class listeners](listeners-classes.md) |
[Filterable listeners](listeners-filterables.md)
