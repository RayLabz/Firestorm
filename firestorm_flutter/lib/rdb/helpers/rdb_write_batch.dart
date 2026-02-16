import 'package:firestorm/rdb/rdb.dart';

import '../../exceptions/null_id_exception.dart';
import '../../firestorm.dart';

///Emulates a write batch (similar to Firestore), for the RDB.
class RDBWriteBatch {

  /// Writes objects to the RDB using a single operation.
  Future<void> create<T>(List<T> objects, { String? subcollection }) async {
    Serializer? serializer = RDB.serializers[T];
    final String? className = RDB.classNames[T];

    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: $T. Consider re-generating Firestorm data classes.');
    }

    final Map<String, dynamic> updates = {};

    //For each object, serialize it, and then add it to the list of updates:
    for (var object in objects) {
      var map = serializer(object);
      if (map["id"] == null || map["id"].isEmpty) {
        throw NullIDException(map);
      }
      String path = RDB.constructPathForClassAndID(className, map["id"], subcollection: subcollection);
      updates[path] = map;
    }

    await RDB.instance.ref().update(updates);
  }

  /// Updates objects in the RDB using a single operation.
  Future<void> update<T>(List<T> objects, { String? subcollection }) async {
    Serializer? serializer = RDB.serializers[T];
    final String? className = RDB.classNames[T];

    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: $T. Consider re-generating Firestorm data classes.');
    }

    final Map<String, dynamic> updates = {};

    //For each object, serialize it, and then add it to the list of updates:
    for (var object in objects) {
      var map = serializer(object);
      if (map["id"] == null || map["id"].isEmpty) {
        throw NullIDException(map);
      }
      String path = RDB.constructPathForClassAndID(className, map["id"], subcollection: subcollection);
      updates[path] = map;
    }

    await RDB.instance.ref().update(updates);
  }

  /// Deletes objects in the RDB using a single operation.
  Future<void> delete<T>(List<T> objects, { String? subcollection }) async {
    Serializer? serializer = RDB.serializers[T];
    final String? className = RDB.classNames[T];

    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    Map<String, dynamic> objectsToDelete = {};

    //For each object, serialize it, and then add it to the list of updates:
    for (var object in objects) {
      var map = serializer(object);
      if (map["id"] == null || map["id"].isEmpty) {
        throw NullIDException(map);
      }
      objectsToDelete[RDB.constructPathForClassAndID(className, map["id"], subcollection: subcollection)] = null;
    }

    await RDB.instance.ref().update(objectsToDelete);
  }

  /// Deletes objects in the RDB using a single operation.
  Future<void> deleteWithIDs(Type type, List<String> ids, { String? subcollection }) async {
    Map<String, dynamic> objectsToDelete = {};
    final String? className = RDB.classNames[type];

    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }

    //For each object, serialize it, and then add it to the list of updates:
    for (final String id in ids) {
      objectsToDelete[RDB.constructPathForClassAndID(className, id, subcollection: subcollection)] = null;
    }

    await RDB.instance.ref().update(objectsToDelete);
  }

}