import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to create documents in Firestore, using transactions.
class FSTransactionCreateDelegate {

  final SetOptions? setOptions;
  final Transaction _tx;

  FSTransactionCreateDelegate.init(this._tx, { this.setOptions });

  /// Creates a document in Firestore from the given object.
  Future<Transaction> one(dynamic object, { String? subcollection }) async {
    final serializer = FS.serializers[object.runtimeType];
    final String? className = FS.classNames[object.runtimeType];
    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    String id = map["id"];
    if (id.isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.instance.collection(className).doc(id);
    if (subcollection != null) {
      ref = FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(id);
    }
    return _tx.set(ref, map, setOptions);
  }

}