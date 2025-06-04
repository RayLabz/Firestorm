import 'package:cloud_firestore/cloud_firestore.dart';

import '../exceptions/null_id_exception.dart';
import 'fs.dart';

/// A delegate class to delete documents from Firestore.
class FSDeleteDelegate {

  /// Deletes a document from Firestore using an object.
  Future<void> one<T>(T object) async {
    final serializer = FS.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('Serializer not registered for type: ${object.runtimeType}');
    }
    final map = serializer(object);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.firestore.collection(object.runtimeType.toString()).doc(map["id"]);
    return await ref.delete();
  }

  /// Deletes multiple documents from Firestore using a list of objects.
  Future<void> many<T>(List<T> objects) async {
    if (objects.isEmpty) return;
    if (objects.length > 500) {
      throw ArgumentError('Batch delete limit exceeded. Maximum 500 objects allowed.');
    }
    final serializer = FS.serializers[objects[0].runtimeType];
    if (serializer == null) {
      throw UnsupportedError('Serializer not registered for type: ${objects[0].runtimeType}');
    }

    WriteBatch batch = FS.firestore.batch();
    for (T object in objects) {
      final map = serializer(object);
      if (map["id"].isEmpty) {
        throw NullIDException(map);
      }
      DocumentReference ref = FS.firestore.collection(object.runtimeType.toString()).doc(map["id"]);
      batch.delete(ref);
    }
    return await batch.commit();
  }

  /// Deletes a document from Firestore by its type and document ID.
  Future<void> oneWithID(Type type, String documentID) async {
    DocumentReference ref = FS.firestore.collection(type.toString()).doc(documentID);
    return await ref.delete();
  }

  /// Deletes multiple documents from Firestore by their type and a list of document IDs.
  Future<void> manyWithIDs(Type type, List<String> documentIDs) async {
    if (documentIDs.isEmpty) return;
    if (documentIDs.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 document IDs allowed.');
    }
    WriteBatch batch = FS.firestore.batch();
    for (String id in documentIDs) {
      DocumentReference ref = FS.firestore.collection(type.toString()).doc(id);
      batch.delete(ref);
    }
    return await batch.commit();
  }

  /// Deletes all documents of a specific type from Firestore.
  Future<void> all(Type type, { required bool iAmSure }) async {
    QuerySnapshot snapshot = await FS.firestore.collection(type.toString()).get();
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