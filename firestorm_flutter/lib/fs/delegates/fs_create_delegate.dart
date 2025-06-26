import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to create documents in Firestore.
class FSCreateDelegate {

  /// Creates a document in Firestore from the given object.
  Future<void> one(dynamic object, { String? subcollection }) async {
    final serializer = FS.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    String id = map["id"];
    if (id.isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.instance.collection(object.runtimeType.toString()).doc(id);
    if (subcollection != null) {
      ref = FS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(id);
    }
    return ref.set(map);
  }

  /// Creates multiple documents in Firestore from a list of objects.
  /// Uses a batch operation for efficiency.
  Future<void> many<T>(List<T> objects, { String? subcollection }) async {
    if (objects.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
    }
    if (objects.isEmpty) {
     return;
    }

    final serializer = FS.serializers[T];

    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${T}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(T);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }

    WriteBatch batch = FS.instance.batch();
    for (var object in objects) {

      DocumentReference documentReference = FS.instance.collection(T.toString()).doc(map["id"]);
      if (subcollection != null) {
        documentReference = FS.instance.collection(T.toString()).doc(subcollection).collection(subcollection).doc(map["id"]);
      }

      batch.set(documentReference, serializer(object));
    }
    return batch.commit();
  }

}