import 'package:cloud_firestore/cloud_firestore.dart';

import '../fs.dart';

/// A delegate class to read documents from Firestore using transactions.
class FSTransactionGetDelegate {

  final Transaction _tx;

  FSTransactionGetDelegate.init(this._tx);

  /// Reads a document from Firestore and converts it to the specified type, while in a transaction.
  Future<T?> one<T>(String documentID, { String? subcollection }) async {
    final deserializer = FS.deserializers[T];
    final String? className = FS.classNames[T];
    if (deserializer == null || className == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    DocumentReference ref = FS.instance.collection(className).doc(documentID);
    if (subcollection != null) {
      ref = FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(documentID);
    }
    DocumentSnapshot snapshot =  await _tx.get(ref);
    if (!snapshot.exists) {
      return Future.error('Document with ID $documentID does not exist in collection $className');
    }
    T object = deserializer(snapshot.data() as Map<String, dynamic>) as T;
    return object;
  }

}