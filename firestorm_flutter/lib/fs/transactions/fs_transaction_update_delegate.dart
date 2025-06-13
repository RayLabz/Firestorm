import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to updates documents on Firestore, using transactions.
class FSTransactionUpdateDelegate {

  final Transaction _tx;

  FSTransactionUpdateDelegate.init(this._tx);

  /// Updates a document in Firestore with the given object.
  Future<Transaction> one<T>(T object, { String? subcollection }) async {
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
    return _tx.update(ref, map);
  }

  //TODO - Implement later, unsupported in v1.
  // /// Updates multiple documents in Firestore with a list of objects.
  // Future<void> many<T>(List<T> objects) async {
  //   if (objects.isEmpty) return Future.value();
  //   if (objects.length > 500) {
  //     throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
  //   }
  //   final serializer = FS.serializers[objects[0].runtimeType];
  //   if (serializer == null) {
  //     throw UnsupportedError('No serializer found for type: ${objects[0].runtimeType}. Consider re-generating Firestorm data classes.');
  //   }
  //   WriteBatch batch = FS.firestore.batch();
  //   for (var object in objects) {
  //     if (object.runtimeType != T) {
  //       throw ArgumentError('All objects must be of type $T');
  //     }
  //     final map = serializer(object);
  //     if (map["id"].isEmpty) {
  //       throw NullIDException(map);
  //     }
  //     DocumentReference ref = FS.firestore.collection(object.runtimeType.toString()).doc(map["id"]);
  //     batch.update(ref, map);
  //   }
  //   return batch.commit();
  // }

}