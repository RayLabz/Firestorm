import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to delete documents from Firestore.
class FSDeleteDelegate {

  /// Deletes a document from Firestore using an object.
  Future<void> one<T>(T object, { String? subcollection }) async {
    final serializer = FS.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.firestore.collection(object.runtimeType.toString()).doc(map["id"]);

    if (subcollection != null) {
      ref = FS.firestore.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(map["id"]);
    }

    return await ref.delete();
  }

  /// Deletes multiple documents from Firestore using a list of objects.
  Future<void> many<T>(List<T> objects, { String? subcollection }) async {
    if (objects.isEmpty) return;
    if (objects.length > 500) {
      throw ArgumentError('Batch delete limit exceeded. Maximum 500 objects allowed.');
    }
    final serializer = FS.serializers[objects[0].runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${objects[0].runtimeType}. Consider re-generating Firestorm data classes.');
    }

    WriteBatch batch = FS.firestore.batch();
    for (T object in objects) {
      final map = serializer(object);
      if (map["id"].isEmpty) {
        throw NullIDException(map);
      }
      DocumentReference ref = FS.firestore.collection(object.runtimeType.toString()).doc(map["id"]);
      if (subcollection != null) {
        ref = FS.firestore.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(map["id"]);
      }
      batch.delete(ref);
    }
    return await batch.commit();
  }

  /// Deletes a document from Firestore by its type and document ID.
  Future<void> oneWithID(Type type, String documentID, { String? subcollection }) async {
    DocumentReference ref = FS.firestore.collection(type.toString()).doc(documentID);
    if (subcollection != null) {
      ref = FS.firestore.collection(type.toString()).doc(subcollection).collection(subcollection).doc(documentID);
    }
    return await ref.delete();
  }

  /// Deletes multiple documents from Firestore by their type and a list of document IDs.
  Future<void> manyWithIDs(Type type, List<String> documentIDs, { String? subcollection }) async {
    if (documentIDs.isEmpty) return;
    if (documentIDs.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 document IDs allowed.');
    }
    WriteBatch batch = FS.firestore.batch();
    for (String id in documentIDs) {
      DocumentReference ref = FS.firestore.collection(type.toString()).doc(id);
      if (subcollection != null) {
        ref = FS.firestore.collection(type.toString()).doc(subcollection).collection(subcollection).doc(id);
      }
      batch.delete(ref);
    }
    return await batch.commit();
  }

  /// Deletes all documents of a specific type from Firestore.
  Future<void> all(Type type, { required bool iAmSure, String? subcollection }) async {
    QuerySnapshot snapshot;
    if (subcollection == null) {
      snapshot = await FS.firestore.collection(type.toString()).get();
    }
    else {
      snapshot = await FS.firestore.collection(type.toString()).doc(subcollection).collection(subcollection).get();
    }
    if (snapshot.docs.isEmpty) return;
    if (snapshot.docs.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 documents allowed.');
    }
    WriteBatch batch = FS.firestore.batch();
    for (QueryDocumentSnapshot doc in snapshot.docs) {
      batch.delete(doc.reference);
    }
    return await batch.commit();
  }

}