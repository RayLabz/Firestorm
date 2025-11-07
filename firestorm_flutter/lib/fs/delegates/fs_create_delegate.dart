import 'package:cloud_firestore/cloud_firestore.dart';

import '../../commons/delegate/create_delegate.dart';
import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to create documents in Firestore.
class FSCreateDelegate implements CreateDelegate {

  /// Creates a document in Firestore from the given object.
  @override
  Future<void> one(dynamic object, { String? subcollection, SetOptions? setOptions }) {
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
    return ref.set(map, setOptions);
  }

  /// Creates multiple documents in Firestore from a list of objects.
  /// Uses a batch operation for efficiency.
  @override
  Future<void> many<T>(List<T> objects, { String? subcollection, SetOptions? setOptions }) {
    if (objects.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
    }
    if (objects.isEmpty) {
     return Future.value();
    }

    final serializer = FS.serializers[objects[0].runtimeType];

    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${objects[0].runtimeType}. Consider re-generating Firestorm data classes.');
    }

    WriteBatch batch = FS.instance.batch();
    for (var object in objects) {

      final map = serializer(object);
      if (map["id"].isEmpty) {
        throw NullIDException(map);
      }

      DocumentReference documentReference = FS.instance.collection(T.toString()).doc(map["id"]);
      if (subcollection != null) {
        documentReference = FS.instance.collection(T.toString()).doc(subcollection).collection(subcollection).doc(map["id"]);
      }

      batch.set(documentReference, serializer(object), setOptions);
    }
    return batch.commit();
  }

}