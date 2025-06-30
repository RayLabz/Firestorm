import 'package:cloud_firestore/cloud_firestore.dart';

import '../fs.dart';

/// A delegate class to read documents from Firestore using transactions.
class FSTransactionGetDelegate {

  final Transaction _tx;

  FSTransactionGetDelegate.init(this._tx);

  /// Reads a document from Firestore and converts it to the specified type, while in a transaction.
  Future<T?> one<T>(String documentID, { String? subcollection }) async {
    final deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    DocumentReference ref = FS.instance.collection(T.toString()).doc(documentID);
    if (subcollection != null) {
      ref = FS.instance.collection(T.toString()).doc(subcollection).collection(subcollection).doc(documentID);
    }
    DocumentSnapshot snapshot =  await _tx.get(ref);
    if (!snapshot.exists) {
      return Future.error('Document with ID $documentID does not exist in collection ${T.toString()}');
    }
    T object = deserializer(snapshot.data() as Map<String, dynamic>) as T;
    return object;
  }

  //TODO  - Implement later, unsupported in v1.
  // /// Reads multiple documents from Firestore and converts them to a list of the specified type.
  // Future<List<T>> many<T>(List<String> documentIDs) async {
  //   final deserializer = FS.deserializers[T];
  //   if (deserializer == null) {
  //     throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
  //   }
  //   List<T> objects = [];
  //   List<DocumentReference> refs = documentIDs.map((id) => FS.firestore.collection(T.toString()).doc(id)).toList();
  //   List<DocumentSnapshot> snapshots = await Future.wait(refs.map((ref) => ref.get()));
  //   for (DocumentSnapshot snapshot in snapshots) {
  //     if (snapshot.exists) {
  //       T object = deserializer(snapshot.data() as Map<String, dynamic>) as T;
  //       objects.add(object);
  //     } else {
  //       print('Document with ID ${snapshot.id} does not exist in collection ${T.toString()}');
  //     }
  //   }
  //   return objects;
  // }

}