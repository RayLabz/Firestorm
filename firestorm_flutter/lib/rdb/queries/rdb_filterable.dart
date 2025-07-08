import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/commons/filterable.dart';
import 'package:firestorm/rdb/helpers/rdb_deserialization_helper.dart';
import 'package:firestorm/rdb/queries/rdb_query_result.dart';

import '../../firestorm.dart';
import '../rdb.dart';


/// An item that helps with filtering objects from Firestore queries.
class RDBFilterable<T> extends Filterable<Query> {

  RDBFilterable(super.query, super.type);

  /// Filters results by equality.
  RDBFilterable<T> equalTo(dynamic value, { String? field }) {
    query = query.equalTo(value, key: field);
    return this;
  }

  /// Orders a query by a specific child field.
  RDBFilterable<T> orderByChild(String field) {
    query = query.orderByChild(field);
    return this;
  }

  /// Orders a query by a specific key.
  RDBFilterable<T> orderByKey() {
    query = query.orderByKey();
    return this;
  }

  /// Orders a query by a specific value.
  RDBFilterable<T> orderByValue() {
    query = query.orderByValue();
    return this;
  }

  /// Starts a query result at a specific value.
  RDBFilterable<T> startAt(dynamic value, { String? field }) {
    query = query.startAt(value, key: field);
    return this;
  }

  /// Ends a query result at a specific value.
  RDBFilterable<T> endAt(dynamic value, { String? field }) {
    query = query.endAt(value, key: field);
    return this;
  }

  /// Limits the result to a certain number of items from the start.
  RDBFilterable<T> limitToFirst(int limit) {
    query = query.limitToFirst(limit);
    return this;
  }

  /// Limits the result to a certain number of items from the end.
  RDBFilterable<T> limitToLast(int limit) {
    query = query.limitToLast(limit);
    return this;
  }

  /// Fetches the results of the query.
  Future<RDBQueryResult<T>> fetch() async {
    Deserializer? deserializer = RDB.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    DataSnapshot snapshot = await query.get();
    List<T> results = [];
    List<DataSnapshot> docs = snapshot.children.toList();
    String? lastID;
    for (DataSnapshot doc in docs) {
      final Map<String, dynamic> map = RDBDeserializationHelper.snapshotToMap(doc);
      T object = deserializer(map);
      results.add(object);
      lastID = map["id"];
    }
    if (results.isEmpty) { //if empty, just return empty list
      return RDBQueryResult<T>(results, null, null);
    }
    else {
      return RDBQueryResult<T>(results, docs[docs.length - 1], lastID);
    }
  }


}