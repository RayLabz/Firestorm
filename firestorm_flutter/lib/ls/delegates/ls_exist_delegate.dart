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
    DocumentRef ref = LS.instance.collection(object.runtimeType.toString()).doc(object.id);
    if (subcollection != null) {
      ref = LS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(object.id);
    }
    Map<String, dynamic>? data = await ref.get();
    return data != null;
  }

  /// Checks if a document exists in Localstore using its type and ID.
  @override
  Future<bool> oneWithID<T>(Type type, String documentID, { String? subcollection }) async {
    DocumentRef ref = LS.instance.collection(type.toString()).doc(documentID);
    if (subcollection != null) {
      ref = LS.instance.collection(type.toString()).doc(subcollection).collection(subcollection).doc(documentID);
    }
    Map<String, dynamic>? data = await ref.get();
    return data != null;
  }

}