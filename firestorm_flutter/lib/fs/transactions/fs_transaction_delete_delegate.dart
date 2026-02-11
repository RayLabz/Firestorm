import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to delete documents from Firestore.
class FSTransactionDeleteDelegate {

  final Transaction _tx;

  FSTransactionDeleteDelegate.init(this._tx);

  /// Deletes a document from Firestore using an object.
  Future<Transaction> one<T>(T object, { String? subcollection }) async {
    final serializer = FS.serializers[object.runtimeType];
    final String? className = FS.classNames[object.runtimeType];
    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.instance.collection(className).doc(map["id"]);
    if (subcollection != null) {
      ref = FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(map["id"]);
    }
    return _tx.delete(ref);
  }

  /// Deletes a document from Firestore by its type and document ID.
  Future<Transaction> oneWithID(Type type, String documentID) async {
    final String? className = FS.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $className. Consider re-generating Firestorm data classes.');
    }
    DocumentReference ref = FS.instance.collection(className).doc(documentID);
    return _tx.delete(ref);
  }

}