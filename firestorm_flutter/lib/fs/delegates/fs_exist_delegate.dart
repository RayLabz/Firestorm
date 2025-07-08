import 'package:cloud_firestore/cloud_firestore.dart';

import '../../commons/delegate/exist_delegate.dart';
import '../fs.dart';

/// A delegate class to check if a document exists in Firestore.
class FSExistDelegate implements ExistDelegate {

  /// Checks if a document exists in Firestore using its type and ID.
  @override
  Future<bool> one<T>(dynamic object, { String? subcollection }) async {
    if (object.id == null) {
      return false;
    }
    DocumentReference ref = FS.instance.collection(object.runtimeType.toString()).doc(object.id);
    if (subcollection != null) {
      ref = FS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(object.id);
    }
    DocumentSnapshot snapshot = await ref.get();
    return snapshot.exists;
  }

  /// Checks if a document exists in Firestore using its type and ID.
  @override
  Future<bool> oneWithID<T>(Type type, String documentID, { String? subcollection }) async {
    DocumentReference ref = FS.instance.collection(type.toString()).doc(documentID);
    if (subcollection != null) {
      ref = FS.instance.collection(type.toString()).doc(subcollection).collection(subcollection).doc(documentID);
    }
    DocumentSnapshot snapshot = await ref.get();
    return snapshot.exists;
  }

}