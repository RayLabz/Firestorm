import 'package:firestorm/commons/delegate/create_delegate.dart';
import 'package:firestorm/rdb/helpers/rdb_write_batch.dart';

import '../../exceptions/null_id_exception.dart';
import '../rdb.dart';

/// A delegate class to create documents in Firestore.
class RDBCreateDelegate implements CreateDelegate {

  /// Creates a document in the Realtime database from the given object.
  @override
  Future<void> one(dynamic object, { String? subcollection }) {
    final serializer = RDB.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    String id = map["id"];
    if (id.isEmpty) {
      throw NullIDException(map);
    }

    final reference = RDB.instance.ref(RDB.constructPathForClassAndID(object.runtimeType, object.id));
    return reference.set(map);
  }

  /// Creates multiple documents in the Realtime database from a list of objects.
  /// Uses a batch operation for efficiency.
  @override
  Future<void> many<T>(List<T> objects, { String? subcollection }) {
    if (objects.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
    }
    RDBWriteBatch batch = RDBWriteBatch();
    return batch.create(objects, subcollection: subcollection);
  }

}