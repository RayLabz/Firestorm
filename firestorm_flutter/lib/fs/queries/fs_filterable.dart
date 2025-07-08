import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/commons/filterable.dart';

import '../../firestorm.dart';
import '../fs.dart';
import 'fs_query_result.dart';

/// An item that helps with filtering objects from Firestore queries.
class FSFilterable<T> extends Filterable<Query> {

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

  /// Streams the results of the query as a stream of type T.
  Stream<T> stream() async* {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No serializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    Stream<QuerySnapshot<Object?>> snapshots = query.snapshots();
    await for (QuerySnapshot<Object?> snapshot in snapshots) {
      for (QueryDocumentSnapshot<Object?> doc in snapshot.docs) {
        yield deserializer(doc.data() as Map<String, dynamic>);
      }
    }
  }

  /// Fetches the results of the query.
  Future<FSQueryResult<T>> fetch() async {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    QuerySnapshot snapshot = await query.get();
    List<T> results = [];
    List<QueryDocumentSnapshot> docs = snapshot.docs;
    String? lastID;
    for (QueryDocumentSnapshot doc in docs) {
      T object = deserializer(doc.data() as Map<String, dynamic>);
      results.add(object);
      lastID = doc.id;
    }
    if (results.isEmpty) { //if empty, just return empty list
      return FSQueryResult<T>(results, null, null);
    }
    else {
      return FSQueryResult<T>(results, docs[docs.length - 1], lastID);
    }
  }

}