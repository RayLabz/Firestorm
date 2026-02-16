import 'package:firebase_database/firebase_database.dart';

import '../../exceptions/null_id_exception.dart';
import '../rdb.dart';

/// Delegate for getting a reference to a document or collection.
class RDBReferenceDelegate {

  /// Returns a reference to a document using its class and document ID.
  DatabaseReference documentFromID(Type type, String documentID, { String? subcollection }) {
    if (documentID.isEmpty) {
      throw NullIDException("Cannot get document reference from an empty ID");
    }
    final String? className = RDB.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }
    return RDB.instance.ref(RDB.constructPathForClassAndID(className, documentID, subcollection: subcollection));
  }

  /// Returns a reference to a document using an object.
  DatabaseReference documentFromObject(dynamic object, { String? subcollection }) {
    if (object == null) {
      throw NullIDException("Cannot get document reference from null object");
    }
    final String? className = RDB.classNames[object.runtimeType];
    if (className == null) {
      throw UnsupportedError('No class name found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }

    return RDB.instance.ref(RDB.constructPathForClassAndID(className, object.id, subcollection: subcollection));
  }

  /// Returns a reference to a document using its path.
  DatabaseReference documentFromPath(String path) {
    if (path.isEmpty) {
      throw NullIDException("Cannot get document reference from an empty path");
    }
    if (!path.contains("/")) {
      throw NullIDException("Cannot get document reference from invalid path: $path");
    }
    return RDB.instance.ref(path);
  }

  /// Returns a reference to a collection using its class.
  DatabaseReference collection(Type type, { String? subcollection }) {
    final String? className = RDB.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }
    return RDB.instance.ref(RDB.constructPathForClass(className, subcollection: subcollection));
  }

}