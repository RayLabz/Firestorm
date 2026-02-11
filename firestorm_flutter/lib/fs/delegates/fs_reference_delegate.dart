import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// Delegate for getting a reference to a document or collection.
class FSReferenceDelegate {

  /// Returns a reference to a document using its class and document ID.
  DocumentReference<Map<String, dynamic>> documentFromID(Type type, String documentID, { String? subcollection }) {
    final String? className = FS.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }
    if (subcollection != null) {
      return FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(documentID);
    }
    return FS.instance.collection(className).doc(documentID);
  }

  /// Returns a reference to a document using an object.
  DocumentReference<Map<String, dynamic>> documentFromObject(dynamic object, { String? subcollection }) {
    if (object == null) {
      throw NullIDException("Cannot get document reference from null object");
    }
    final String? className = FS.classNames[object.runtimeType];
    if (className == null) {
      throw UnsupportedError('No class name found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }

    if (subcollection != null) {
      return FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(object.id);
    }
    return FS.instance.collection(className).doc(object.id);
  }

  /// Returns a reference to a document using its path.
  DocumentReference<Map<String, dynamic>> documentFromPath(String path) {
    if (path.isEmpty) {
      throw NullIDException("Cannot get document reference from an empty path");
    }
    if (!path.contains("/")) {
      throw NullIDException("Cannot get document reference from invalid path: $path");
    }
    return FS.instance.doc(path);
  }

  /// Returns a reference to a collection using its class.
  CollectionReference<Map<String, dynamic>> collection(Type type, { String? subcollection }) {
    final String? className = FS.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $className. Consider re-generating Firestorm data classes.');
    }
    if (subcollection != null) {
      return FS.instance.collection(className).doc(subcollection).collection(subcollection);
    }
    return FS.instance.collection(className);
  }

}