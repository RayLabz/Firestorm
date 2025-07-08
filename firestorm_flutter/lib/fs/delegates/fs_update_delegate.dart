import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/commons/delegate/update_delegate.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to updates documents on Firestore.
class FSUpdateDelegate implements UpdateDelegate {

  /// Updates a document in Firestore with the given object.
  @override
  Future<void> one<T>(T object, { String? subcollection }) {
    final serializer = FS.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.instance.collection(object.runtimeType.toString()).doc(map["id"]);
    if (subcollection != null) {
      ref = FS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(map["id"]);
    }
    return ref.update(map);
  }

  /// Updates multiple documents in Firestore with a list of objects.
  @override
  Future<void> many<T>(List<T> objects, { String? subcollection }) {
    if (objects.isEmpty) return Future.value();
    if (objects.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
    }
    final serializer = FS.serializers[objects[0].runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${objects[0].runtimeType}. Consider re-generating Firestorm data classes.');
    }
    WriteBatch batch = FS.instance.batch();
    for (var object in objects) {
      if (object.runtimeType != T) {
        throw ArgumentError('All objects must be of type $T');
      }
      final map = serializer(object);
      if (map["id"].isEmpty) {
        throw NullIDException(map);
      }
      DocumentReference ref = FS.instance.collection(object.runtimeType.toString()).doc(map["id"]);
      if (subcollection != null) {
        ref = FS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(map["id"]);
      }
      batch.update(ref, map);
    }
    return batch.commit();
  }

}