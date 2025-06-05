import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to create documents in Firestore, using transactions.
class FSTransactionCreateDelegate {

  final Transaction _tx;

  FSTransactionCreateDelegate.init(this._tx);

  /// Creates a document in Firestore from the given object.
  Future<Transaction> one(dynamic object) async {
    final serializer = FS.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    String id = map["id"];
    if (id.isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.firestore.collection(object.runtimeType.toString()).doc(id);
    return _tx.set(ref, map);
  }

  //TODO - Implement later, unsupported in v1.
  // /// Creates multiple documents in Firestore from a list of objects.
  // /// Uses a batch operation for efficiency.
  // Future<void> many<T>(List<T> objects) async {
  //   if (objects.length > 500) {
  //     throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
  //   }
  //   WriteBatch batch = FS.firestore.batch();
  //   for (var object in objects) {
  //     final serializer = FS.serializers[object.runtimeType];
  //
  //     if (serializer == null) {
  //       throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
  //     }
  //     final map = serializer(object);
  //     if (map["id"].isEmpty) {
  //       throw NullIDException(map);
  //     }
  //
  //     final DocumentReference documentReference = FS.firestore.collection(T.toString()).doc(map["id"]);
  //
  //     batch.set(documentReference, serializer(object));
  //   }
  //   return batch.commit();
  // }

}