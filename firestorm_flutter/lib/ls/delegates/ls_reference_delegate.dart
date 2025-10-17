import 'package:firestorm/ls/ls.dart';
import 'package:localstore/localstore.dart';

import '../../exceptions/null_id_exception.dart';

/// Delegate for getting a reference to a document or collection.
class LSReferenceDelegate {

  /// Returns a reference to a document using its class and document ID.
  DocumentRef documentFromID(Type type, String documentID, { String? subcollection }) {
    if (subcollection != null) {
      return LS.instance.collection(type.toString()).doc(subcollection).collection(subcollection).doc(documentID);
    }
    return LS.instance.collection(type.toString()).doc(documentID);
  }

  /// Returns a reference to a document using an object.
  DocumentRef documentFromObject(dynamic object, { String? subcollection }) {
    if (object == null) {
      throw NullIDException("Cannot get document reference from null object");
    }
    if (subcollection != null) {
      return LS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(object.id);
    }
    return LS.instance.collection(object.runtimeType.toString()).doc(object.id);
  }

  /// Returns a reference to a collection using its class.
  CollectionRef collection(Type type, { String? subcollection }) {
    if (subcollection != null) {
      return LS.instance.collection(type.toString()).doc(subcollection).collection(subcollection);
    }
    return LS.instance.collection(type.toString());
  }

}