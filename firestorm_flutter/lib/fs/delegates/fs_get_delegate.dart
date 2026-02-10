import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/commons/delegate/get_delegate.dart';

import '../fs.dart';

/// A delegate class to read documents from Firestore.
class FSGetDelegate implements GetDelegate {

  /// Reads a document from Firestore and converts it to the specified type.\
  @override
  Future<T?> one<T>(String documentID, { String? subcollection, GetOptions? getOptions }) async {
    final deserializer = FS.deserializers[T];
    final String? className = FS.classNames[T];

    if (deserializer == null || className == null) {
      throw UnsupportedError('No deserializer/class name found for type: $T. Consider re-generating Firestorm data classes.');
    }

    DocumentReference ref = FS.instance.collection(className).doc(documentID);
    if (subcollection != null) {
      ref = FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(documentID);
    }

    DocumentSnapshot snapshot = await ref.get(getOptions);
    if (!snapshot.exists) {
      return null;
    }
    T object = deserializer(snapshot.data() as Map<String, dynamic>) as T;
    return object;
  }

  /// Reads multiple documents from Firestore and converts them to a list of the specified type.
  /// Sends multiple requests at the same time and returns when all are completed.
  @override
  Future<List<T>> many<T>(List<String> documentIDs, { String? subcollection, GetOptions? getOptions }) async {
    final deserializer = FS.deserializers[T];
    final String? className = FS.classNames[T];

    if (deserializer == null || className == null) {
      throw UnsupportedError('No deserializer/class name found for type: $T. Consider re-generating Firestorm data classes.');
    }
    List<T> objects = [];
    List<DocumentReference> refs;
    if (subcollection != null) {
      refs = documentIDs.map((id) => FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(id)).toList();
    }
    else {
      refs = documentIDs.map((id) => FS.instance.collection(className).doc(id)).toList();
    }
    List<DocumentSnapshot> snapshots = await Future.wait(refs.map((ref) => ref.get(getOptions)));
    for (DocumentSnapshot snapshot in snapshots) {
      if (snapshot.exists) {
        T object = deserializer(snapshot.data() as Map<String, dynamic>) as T;
        objects.add(object);
      }
    }
    return objects;
  }


}