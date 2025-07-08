import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/commons/delegate/delete_delegate.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to delete documents from Firestore.
class FSDeleteDelegate implements DeleteDelegate {

  /// Deletes a document from Firestore using an object.
  @override
  Future<void> one(dynamic object, { String? subcollection }) {
    final serializer = FS.serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('No serializer found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.instance.collection(object.runtimeType.toString()).doc(map["id"]);

    if (subcollection != null) {
      ref = FS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(map["id"]);
    }

    return ref.delete();
  }

  /// Deletes multiple documents from Firestore using a list of objects.
  @override
  Future<void> many<T>(List<T> objects, { String? subcollection }) {
    if (objects.isEmpty) return Future.value();
    if (objects.length > 500) {
      throw ArgumentError('Batch delete limit exceeded. Maximum 500 objects allowed.');
    }

    List<Future<void>> futures = [];
    for (dynamic object in objects) {
      if (object.id == null) {
        continue; //skip
      }
      DocumentReference ref = FS.instance.collection(object.runtimeType.toString()).doc(object.id);
      if (subcollection != null) {
        ref = FS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(object.id);
      }
      futures.add(ref.delete());
    }
    return Future.wait(futures);
  }

  /// Deletes a document from Firestore by its type and document ID.
  @override
  Future<void> oneWithID(Type type, String documentID, { String? subcollection }) {
    DocumentReference ref = FS.instance.collection(type.toString()).doc(documentID);
    if (subcollection != null) {
      ref = FS.instance.collection(type.toString()).doc(subcollection).collection(subcollection).doc(documentID);
    }
    return ref.delete();
  }

  /// Deletes multiple documents from Firestore by their type and a list of document IDs.
  @override
  Future<void> manyWithIDs(Type type, List<String> documentIDs, { String? subcollection }) async {
    if (documentIDs.isEmpty) return;
    if (documentIDs.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 document IDs allowed.');
    }
    WriteBatch batch = FS.instance.batch();
    for (String id in documentIDs) {
      DocumentReference ref = FS.instance.collection(type.toString()).doc(id);
      if (subcollection != null) {
        ref = FS.instance.collection(type.toString()).doc(subcollection).collection(subcollection).doc(id);
      }
      batch.delete(ref);
    }
    return batch.commit();
  }

  /// Deletes all documents of a specific type from Firestore.
  @override
  Future<void> all(Type type, { required bool iAmSure, String? subcollection }) async {
    if (iAmSure) {
      QuerySnapshot snapshot;
      if (subcollection == null) {
        snapshot = await FS.instance.collection(type.toString()).get();
      }
      else {
        snapshot =
        await FS.instance.collection(type.toString()).doc(subcollection)
            .collection(subcollection)
            .get();
      }
      if (snapshot.docs.isEmpty) return Future.value();
      if (snapshot.docs.length > 500) {
        throw ArgumentError(
            'Batch limit exceeded. Maximum 500 documents allowed.');
      }
      WriteBatch batch = FS.instance.batch();
      for (QueryDocumentSnapshot doc in snapshot.docs) {
        batch.delete(doc.reference);
      }
      return batch.commit();
    }
  }

}