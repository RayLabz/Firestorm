import 'package:firestorm/rdb/rdb.dart';

import '../../exceptions/null_id_exception.dart';
import '../../firestorm.dart';

///Emulates a write batch (similar to Firestore), for the RDB.
class RDBWriteBatch {

  /// Writes objects to the RDB using a single operation.
  Future<void> create<T>(List<T> objects, { String? subcollection }) async {
    Serializer? serializer = RDB.serializers[T];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    final Map<String, dynamic> updates = {};

    //For each object, serialize it, and then add it to the list of updates:
    for (var object in objects) {
      var map = serializer(object);
      if (map["id"] == null || map["id"].isEmpty) {
        throw NullIDException(map);
      }
      String path = RDB.constructPathForClassAndID(object.runtimeType, map["id"], subcollection: subcollection);
      updates[path] = map;
    }

    await RDB.rdb.ref().update(updates);
  }

  /// Updates objects in the RDB using a single operation.
  Future<void> update<T>(List<T> objects, { String? subcollection }) async {
    Serializer? serializer = RDB.serializers[T];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    final Map<String, dynamic> updates = {};

    //For each object, serialize it, and then add it to the list of updates:
    for (var object in objects) {
      var map = serializer(object);
      if (map["id"] == null || map["id"].isEmpty) {
        throw NullIDException(map);
      }
      String path = RDB.constructPathForClassAndID(object.runtimeType, map["id"], subcollection: subcollection);
      updates[path] = map;
    }

    await RDB.rdb.ref().update(updates);
  }

  /// Deletes objects in the RDB using a single operation.
  Future<void> delete<T>(List<T> objects, { String? subcollection }) async {
    Serializer? serializer = RDB.serializers[T];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    Map<String, dynamic> objectsToDelete = {};

    //For each object, serialize it, and then add it to the list of updates:
    for (var object in objects) {
      var map = serializer(object);
      if (map["id"] == null || map["id"].isEmpty) {
        throw NullIDException(map);
      }
      objectsToDelete[RDB.constructPathForClassAndID(object.runtimeType, map["id"], subcollection: subcollection)] = null;
    }

    await RDB.rdb.ref().update(objectsToDelete);
  }

  /// Deletes objects in the RDB using a single operation.
  Future<void> deleteWithIDs(Type type, List<String> ids, { String? subcollection }) async {
    Map<String, dynamic> objectsToDelete = {};

    //For each object, serialize it, and then add it to the list of updates:
    for (final String id in ids) {
      objectsToDelete[RDB.constructPathForClassAndID(type, id, subcollection: subcollection)] = null;
    }

    await RDB.rdb.ref().update(objectsToDelete);
  }

}