import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/filterable.dart';

/// An item that helps with filtering objects from Firestore queries.
class FSFilterable<T> extends Filterable<Query, T> {

  FSFilterable(super.query, super.type);

  /// Filters results by equality.
  FSFilterable<T> whereEqualTo(String field, dynamic value) {
    query = query.where(field, isEqualTo: value);
    return this;
  }

  /// Filters results by inequality.
  FSFilterable<T> whereNotEqualTo(String field, dynamic value) {
    query = query.where(field, isNotEqualTo: value);
    return this;
  }

  /// Filters results by value (less than).
  FSFilterable<T> whereLessThan(String field, dynamic value) {
    query = query.where(field, isLessThan: value);
    return this;
  }

  /// Filters results by value (less than or equal to).
  FSFilterable<T> whereLessThanOrEqualTo(String field, dynamic value) {
    query = query.where(field, isLessThanOrEqualTo: value);
    return this;
  }

  /// Filters results by value (greater than).
  FSFilterable<T> whereGreaterThan(String field, dynamic value) {
    query = query.where(field, isGreaterThan: value);
    return this;
  }

  /// Filters results by value (greater than or equal to).
  FSFilterable<T> whereGreaterThanOrEqualTo(String field, dynamic value) {
    query = query.where(field, isGreaterThanOrEqualTo: value);
    return this;
  }

  /// Filters results by array field containing a value.
  FSFilterable<T> whereArrayContains(String field, dynamic value) {
    query = query.where(field, arrayContains: value);
    return this;
  }

  /// Filters results by array field containing a value.
  FSFilterable<T> whereArrayContainsAny(String field, List<dynamic> values) {
    query = query.where(field, arrayContainsAny: values);
    return this;
  }

  /// Filters results by array field containing any of the provided values.
  FSFilterable<T> whereIn(String field, List<dynamic> values) {
    query = query.where(field, whereIn: values);
    return this;
  }

  /// Filters by an array field NOT containing any of the provided values.
  FSFilterable<T> whereNotIn(String field, List<dynamic> values) {
    query = query.where(field, whereNotIn: values);
    return this;
  }

  /// Orders the query results by a field's value.
  FSFilterable<T> orderBy(String field, { bool descending = false }) {
    query = query.orderBy(field, descending: descending);
    return this;
  }

  /// Limits the number of results.
  FSFilterable<T> limit(int limit) {
    query = query.limit(limit);
    return this;
  }

  /// Starts the query at a document where the field values provided are met.
  FSFilterable<T> startAt(List<dynamic> fieldValues) {
    query = query.startAt(fieldValues);
    return this;
  }

  /// Starts the query after a specific document where the field values provided are met.
  FSFilterable<T> startAfter(List<dynamic> fieldValues) {
    query = query.startAfter(fieldValues);
    return this;
  }

  /// Ends the query before a specific document where the field values provided are met.
  FSFilterable<T> endBefore(List<dynamic> fieldValues) {
    query = query.endBefore(fieldValues);
    return this;
  }

  /// Ends the query at a specific document where the field values provided are met.
  FSFilterable<T> endAt(List<dynamic> fieldValues) {
    query = query.endAt(fieldValues);
    return this;
  }

  // Stream<T> stream() {
  //   Stream<QuerySnapshot<Object?>> snapshots = query.snapshots();
  //   Stream<T> objectStream = Stream.empty();
  //
  //
  //   return this;
  // }




  //
  // /**
  //  * Streams the query to an observer.
  //  * @param responseObserver The observer to stream the query to.
  //  */
  // public void stream(@Nonnull ApiStreamObserver<DocumentSnapshot> responseObserver) {
  // query.stream(responseObserver);
  // }
  //
  // /**
  //  * Retrieves the query snapshot.
  //  * @return Returns an ApiFuture of type QuerySnapshot.
  //  */
  // @Nonnull
  // public ApiFuture<QuerySnapshot> get() {
  // return query.get();
  // }
  //
  // /**
  //  * Adds an event listener to a snapshot.
  //  * @param executor The executor of the event.
  //  * @param listener The listener to add.
  //  * @return Returns a ListenerRegistration.
  //  */
  // @Nonnull
  // public ListenerRegistration addSnapshotListener(@Nonnull Executor executor, @Nonnull EventListener<QuerySnapshot> listener) {
  // return query.addSnapshotListener(executor, listener);
  // }
  //
  // /**
  //  * Adds an event listener to a snapshot.
  //  * @param listener The listener to add.
  //  * @return Returns a ListenerRegistration.
  //  */
  // @Nonnull
  // public ListenerRegistration addSnapshotListener(@Nonnull EventListener<QuerySnapshot> listener) {
  // return query.addSnapshotListener(listener);
  // }
  //
  // /**
  //  * Retrieves a hash code for the query.
  //  * @return Returns an integer hash code.
  //  */
  // public int hashCode() {
  // return query.hashCode();
  // }
  //
  // /**
  //  * Fetches the results of a filterable.
  //  * @return An ArrayList containing the results of a filter.
  //  */
  // public FSFuture<FSQueryResult<T>> fetch() {
  // ApiFuture<QuerySnapshot> future = query.get();
  // ApiFuture<FSQueryResult<T>> queryFuture = ApiFutures.transform(future, input -> {
  // List<QueryDocumentSnapshot> documents = input.getDocuments();
  // ArrayList<T> documentList = new ArrayList<>();
  // String lastID = null;
  // for (final QueryDocumentSnapshot document : documents) {
  // T object = document.toObject(objectClass);
  // documentList.add(object);
  // lastID = document.getId();
  // }
  // if (documentList.isEmpty()) {
  // return new FSQueryResult<>(documentList, null, null);
  // }
  // else {
  // return new FSQueryResult<>(documentList, documents.get(documents.size() - 1), lastID);
  // }
  // }, Firestorm.getSelectedExecutor());
  // return FSFuture.fromAPIFuture(queryFuture);
  // }

}