import 'package:firestorm/rdb/rdb_write_batch.dart';

import '../../exceptions/null_id_exception.dart';
import '../rdb.dart';

/// A delegate class to delete documents in RDB.
class RDBDeleteDelegate {

  /// Deletes a document in RDB from the given object.
  Future<void> one(dynamic object, { String? subcollection }) async {
    final serializer = RDB.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    String id = map["id"];
    if (id.isEmpty) {
      throw NullIDException(map);
    }

    final reference = RDB.rdb.ref(RDB.constructPathForClassAndID(object.runtimeType, id, subcollection: subcollection));
    return reference.remove();
  }

  /// Deletes multiple documents from RDB from a list of objects.
  /// Uses a batch operation for efficiency.
  Future<void> many<T>(List<T> objects, { String? subcollection }) async {
    if (objects.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
    }
    RDBWriteBatch batch = RDBWriteBatch();
    return batch.delete(objects, subcollection: subcollection);
  }

  /// Deletes a document from RDB by its type and document ID.
  Future<void> oneWithID(Type type, String documentID, { String? subcollection }) async {
    String path = RDB.constructPathForClassAndID(type, documentID, subcollection: subcollection);
    final reference = RDB.rdb.ref(path);
    return reference.remove();
  }

  /// Deletes multiple documents from the RDB by their type and a list of document IDs.
  Future<void> manyWithIDs(Type type, List<String> documentIDs, { String? subcollection }) async {
    if (documentIDs.isEmpty) return;
    if (documentIDs.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 document IDs allowed.');
    }
    RDBWriteBatch batch = RDBWriteBatch();
    return batch.deleteWithIDs(type, documentIDs, subcollection: subcollection);
  }

  /// Deletes all documents of a specific type from Firestore.
  Future<void> all(Type type, { required bool iAmSure, String? subcollection }) async {
    if (iAmSure) {
      //Get the objects of this type:
      final reference = RDB.rdb.ref(type.toString());
      final snapshot = await reference.once();
      if (snapshot.snapshot.value == null) {
        return; // No documents to delete
      }

      final Map<String, dynamic> updates = {};

      final data = snapshot.snapshot.value as Map<String, dynamic>;
      data.forEach((key, value) {
        String path = RDB.constructPathForClassAndID(type, key, subcollection: subcollection);
        updates[path] = null; // Mark for deletion
      });
      // Perform the deletion in a single update operation:
      return await reference.update(updates);
    }
  }

}