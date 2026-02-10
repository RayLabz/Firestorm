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
    final String? className = FS.classNames[object.runtimeType];
    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: ${className}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.instance.collection(className).doc(map["id"]);
    if (subcollection != null) {
      ref = FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(map["id"]);
    }
    return _tx.update(ref, map);
  }

}