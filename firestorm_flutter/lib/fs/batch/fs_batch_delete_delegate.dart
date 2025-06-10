import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to delete documents from Firestore, using batches.
class FSBatchDeleteDelegate {

  final WriteBatch _batch;

  FSBatchDeleteDelegate.init(this._batch);

  /// Deletes a document from Firestore using an object.
  one<T>(T object, { String? subcollection }) async {
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
    return _batch.delete(ref);
  }

  //TODO - Implement later, unsupported in v1.
  // /// Deletes multiple documents from Firestore using a list of objects.
  // Future<void> many<T>(List<T> objects) async {
  //   if (objects.isEmpty) return;
  //   if (objects.length > 500) {
  //     throw ArgumentError('Batch delete limit exceeded. Maximum 500 objects allowed.');
  //   }
  //   final serializer = FS.serializers[objects[0].runtimeType];
  //   if (serializer == null) {
  //     throw UnsupportedError('No serializer found for type: ${objects[0].runtimeType}. Consider re-generating Firestorm data classes.');
  //   }
  //
  //   WriteBatch batch = FS.firestore.batch();
  //   for (T object in objects) {
  //     final map = serializer(object);
  //     if (map["id"].isEmpty) {
  //       throw NullIDException(map);
  //     }
  //     DocumentReference ref = FS.firestore.collection(object.runtimeType.toString()).doc(map["id"]);
  //     batch.delete(ref);
  //   }
  //   return await batch.commit();
  // }

  /// Deletes a document from Firestore by its type and document ID.
  oneWithID(Type type, String documentID) async {
    DocumentReference ref = FS.firestore.collection(type.toString()).doc(documentID);
    return _batch.delete(ref);
  }

  // TODO - Implement later, unsupported in v1.
  // /// Deletes multiple documents from Firestore by their type and a list of document IDs.
  // Future<void> manyWithIDs(Type type, List<String> documentIDs) async {
  //   if (documentIDs.isEmpty) return;
  //   if (documentIDs.length > 500) {
  //     throw ArgumentError('Batch limit exceeded. Maximum 500 document IDs allowed.');
  //   }
  //   WriteBatch batch = FS.firestore.batch();
  //   for (String id in documentIDs) {
  //     DocumentReference ref = FS.firestore.collection(type.toString()).doc(id);
  //     batch.delete(ref);
  //   }
  //   return await batch.commit();
  // }

  //TODO - Implement later, unsupported in v1.
  // /// Deletes all documents of a specific type from Firestore.
  // Future<void> all(Type type, { required bool iAmSure }) async {
  //   QuerySnapshot snapshot = await FS.firestore.collection(type.toString()).get();
  //   if (snapshot.docs.isEmpty) return;
  //   if (snapshot.docs.length > 500) {
  //     throw ArgumentError('Batch limit exceeded. Maximum 500 documents allowed.');
  //   }
  //   WriteBatch batch = FS.firestore.batch();
  //   for (QueryDocumentSnapshot doc in snapshot.docs) {
  //     batch.delete(doc.reference);
  //   }
  //   return await batch.commit();
  // }

}