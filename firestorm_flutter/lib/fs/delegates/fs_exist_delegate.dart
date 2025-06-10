import 'package:cloud_firestore/cloud_firestore.dart';

import '../fs.dart';

/// A delegate class to check if a document exists in Firestore.
class FSExistDelegate {

  /// Checks if a document exists in Firestore.
  Future<bool> one<T>(Type type, String documentID, { String? subcollection }) async {
    DocumentReference ref = FS.firestore.collection(type.toString()).doc(documentID);
    if (subcollection != null) {
      ref = FS.firestore.collection(type.toString()).doc(subcollection).collection(subcollection).doc(documentID);
    }
    DocumentSnapshot snapshot = await ref.get();
    return snapshot.exists;
  }

}