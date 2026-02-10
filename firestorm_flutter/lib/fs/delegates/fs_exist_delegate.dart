import 'package:cloud_firestore/cloud_firestore.dart';

import '../../commons/delegate/exist_delegate.dart';
import '../fs.dart';

/// A delegate class to check if a document exists in Firestore.
class FSExistDelegate implements ExistDelegate {

  /// Checks if a document exists in Firestore using its type and ID.
  @override
  Future<bool> one<T>(dynamic object, { String? subcollection, GetOptions? getOptions }) async {
    if (object.id == null) {
      return false;
    }
    final String? className = FS.classNames[T];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $className. Consider re-generating Firestorm data classes.');
    }
    DocumentReference ref = FS.instance.collection(className).doc(object.id);
    if (subcollection != null) {
      ref = FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(object.id);
    }
    DocumentSnapshot snapshot = await ref.get(getOptions);
    return snapshot.exists;
  }

  /// Checks if a document exists in Firestore using its type and ID.
  @override
  Future<bool> oneWithID<T>(Type type, String documentID, { String? subcollection, GetOptions? getOptions }) async {
    final String? className = FS.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    DocumentReference ref = FS.instance.collection(className).doc(documentID);
    if (subcollection != null) {
      ref = FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(documentID);
    }
    DocumentSnapshot snapshot = await ref.get(getOptions);
    return snapshot.exists;
  }

}