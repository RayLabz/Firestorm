import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/commons/delegate/list_delegate.dart';
import 'package:firestorm/rdb/helpers/rdb_deserialization_helper.dart';

import '../queries/rdb_filterable.dart';
import '../rdb.dart';

/// A delegate to list items.
class RDBListDelegate implements ListDelegate {

  /// Lists a limited number of items of a specific type without a query.
  @override
  Future<List<T>> ofClass<T>(Type type, { int limit = 10, String? subcollection }) async {
    if (T.toString() != type.toString()) {
      throw ArgumentError("Type mismatch. Attempting to list items of type '${T.toString()}', but parameter type was ${type.toString()}");
    }

    final deserializer = RDB.deserializers[type];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $type. Consider re-generating Firestorm data classes.');
    }

    List<T> objects = [];
    final String path = RDB.constructPathForClass(type, subcollection: subcollection);
    final Query query = RDB.instance.ref(path).limitToFirst(limit);
    final DataSnapshot snapshot = await query.get();
    if (snapshot.exists) {
      for (final childSnapshot in snapshot.children) {
        final Map<String, dynamic> map = RDBDeserializationHelper.snapshotToMap(childSnapshot);
        objects.add(deserializer(map));
      }
    }
    return objects;
  }

  /// Lists all items of a specific type.
  @override
  Future<List<T>> allOfClass<T>(Type type, { String? subcollection }) async {
    if (T.toString() != type.toString()) {
      throw ArgumentError("Type mismatch. Attempting to list items of type '${T.toString()}', but parameter type was ${type.toString()}");
    }

    final deserializer = RDB.deserializers[type];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $type. Consider re-generating Firestorm data classes.');
    }

    List<T> objects = [];
    final String path = RDB.constructPathForClass(type, subcollection: subcollection);
    final Query query = RDB.instance.ref(path);
    final DataSnapshot snapshot = await query.get();
    if (snapshot.exists) {
      for (final childSnapshot in snapshot.children) {
        if (childSnapshot.exists) {
          final Map<String, dynamic> map = RDBDeserializationHelper.snapshotToMap(childSnapshot);
          objects.add(deserializer(map));
        }
      }
    }
    return objects;
  }

  /// Applies a filter to a specific type of items and returns a list of items.
  RDBFilterable<T> filter<T>(Type type, { String? subcollection }) {
    final String path = RDB.constructPathForClass(type, subcollection: subcollection);
    DatabaseReference reference = RDB.instance.ref(path);
    return RDBFilterable<T>(reference, type);
  }

}