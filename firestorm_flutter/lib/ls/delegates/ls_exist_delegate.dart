import 'package:firestorm/ls/ls.dart';
import 'package:localstore/localstore.dart';

import '../../commons/delegate/exist_delegate.dart';

/// A delegate class to check if a document exists in Localstore.
class LSExistDelegate implements ExistDelegate {

  /// Checks if a document exists in Localstore using its type and ID.
  @override
  Future<bool> one<T>(dynamic object, { String? subcollection }) async {
    if (object.id == null) {
      return false;
    }
    final String? className = LS.classNames[object.runtimeType];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $className. Consider re-generating Firestorm data classes.');
    }

    DocumentRef ref = LS.instance.collection(className).doc(object.id);
    if (subcollection != null) {
      ref = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(object.id);
    }
    Map<String, dynamic>? data = await ref.get();
    return data != null;
  }

  /// Checks if a document exists in Localstore using its type and ID.
  @override
  Future<bool> oneWithID<T>(Type type, String documentID, { String? subcollection }) async {
    final String? className = LS.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }

    DocumentRef ref = LS.instance.collection(className).doc(documentID);
    if (subcollection != null) {
      ref = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(documentID);
    }
    Map<String, dynamic>? data = await ref.get();
    return data != null;
  }

}