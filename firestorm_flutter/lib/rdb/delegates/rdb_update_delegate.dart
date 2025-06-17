import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/rdb/helpers/rdb_write_batch.dart';

import '../../exceptions/null_id_exception.dart';
import '../rdb.dart';

/// A delegate class to updates documents on Firestore.
class RDBUpdateDelegate {

  /// Updates a document in Firestore with the given object.
  Future<void> one<T>(T object, { String? subcollection }) async {
    final serializer = RDB.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }
    final String path = RDB.constructPathForClassAndID(object.runtimeType, map["id"], subcollection: subcollection);
    DatabaseReference ref = RDB.instance.ref(path);
    await ref.update(map);
  }

  /// Updates multiple documents in Firestore with a list of objects.
  Future<void> many<T>(List<T> objects, { String? subcollection }) async {
    if (objects.isEmpty) return Future.value();
    if (objects.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
    }
    final serializer = RDB.serializers[objects[0].runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${objects[0].runtimeType}. Consider re-generating Firestorm data classes.');
    }

    RDBWriteBatch batch = RDBWriteBatch();
    return batch.update(objects, subcollection: subcollection);
  }

}