import 'package:firestorm/ls/ls.dart';
import 'package:localstore/localstore.dart';

import '../../exceptions/null_id_exception.dart';

/// Delegate for getting a reference to a document or collection.
class LSReferenceDelegate {

  /// Returns a reference to a document using its class and document ID.
  DocumentRef documentFromID(Type type, String documentID, { String? subcollection }) {
    final String? className = LS.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }
    if (subcollection != null) {
      return LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(documentID);
    }
    return LS.instance.collection(className).doc(documentID);
  }

  /// Returns a reference to a document using an object.
  DocumentRef documentFromObject(dynamic object, { String? subcollection }) {
    if (object == null) {
      throw NullIDException("Cannot get document reference from null object");
    }
    final String? className = LS.classNames[object.runtimeType];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $className. Consider re-generating Firestorm data classes.');
    }
    if (subcollection != null) {
      return LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(object.id);
    }
    return LS.instance.collection(className).doc(object.id);
  }

  /// Returns a reference to a collection using its class.
  CollectionRef collection(Type type, { String? subcollection }) {
    final String? className = LS.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }
    if (subcollection != null) {
      return LS.instance.collection(className).doc(subcollection).collection(subcollection);
    }
    return LS.instance.collection(className);
  }

}