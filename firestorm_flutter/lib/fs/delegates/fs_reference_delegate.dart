import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// Delegate for getting a reference to a document or collection.
class FSReferenceDelegate {

  /// Returns a reference to a document using its class and document ID.
  DocumentReference<Map<String, dynamic>> documentFromID(Type type, String documentID) {
    return FS.firestore.collection(type.toString()).doc(documentID);
  }

  /// Returns a reference to a document using an object.
  DocumentReference<Map<String, dynamic>> documentFromObject(dynamic object) {
    if (object == null) {
      throw NullIDException("Cannot get document reference from null object");
    }
    return FS.firestore.collection(object.runtimeType.toString()).doc(object.id);
  }

  /// Returns a reference to a document using its path.
  DocumentReference<Map<String, dynamic>> documentFromPath(String path) {
    if (path.isEmpty) {
      throw NullIDException("Cannot get document reference from an empty path");
    }
    if (!path.contains("/")) {
      throw NullIDException("Cannot get document reference from invalid path: $path");
    }
    return FS.firestore.doc(path);
  }

  /// Returns a reference to a collection using its class.
  CollectionReference<Map<String, dynamic>> collection(Type type) {
    return FS.firestore.collection(type.toString());
  }

}