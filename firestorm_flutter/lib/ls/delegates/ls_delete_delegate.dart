import 'package:firestorm/commons/delegate/delete_delegate.dart';
import 'package:localstore/localstore.dart';

import '../../exceptions/null_id_exception.dart';
import '../ls.dart';

/// A delegate class to delete documents from Firestore.
class LSDeleteDelegate implements DeleteDelegate {

  /// Deletes a document from Firestore using an object.
  @override
  Future<void> one(dynamic object, { String? subcollection }) {
    final serializer = LS.serializers[object.runtimeType];
    final String? className = LS.classNames[object.runtimeType];
    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: $className. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    if (map["id"].isEmpty) {
      throw NullIDException(map);
    }
    DocumentRef ref = LS.instance.collection(className).doc(map["id"]);

    if (subcollection != null) {
      ref = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(map["id"]);
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

    final String? className = LS.classNames[objects[0].runtimeType];
    if (className == null) {
      throw UnsupportedError('No class name found for type: ${objects[0].runtimeType}. Consider re-generating Firestorm data classes.');
    }

    List<Future<void>> futures = [];
    for (dynamic object in objects) {
      if (object.id == null) {
        continue; //skip
      }
      DocumentRef ref = LS.instance.collection(className).doc(object.id);
      if (subcollection != null) {
        ref = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(object.id);
      }
      futures.add(ref.delete());
    }
    return Future.wait(futures);
  }

  /// Deletes a document from Firestore by its type and document ID.
  @override
  Future<void> oneWithID(Type type, String documentID, { String? subcollection }) {
    final String? className = LS.classNames[type.runtimeType];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }

    DocumentRef ref = LS.instance.collection(className).doc(documentID);
    if (subcollection != null) {
      ref = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(documentID);
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

    final String? className = LS.classNames[type.runtimeType];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }

    List<Future> futures = [];

    for (String id in documentIDs) {
      DocumentRef ref = LS.instance.collection(className).doc(id);
      if (subcollection != null) {
        ref = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(id);
      }
      futures.add(ref.delete());
    }
    await Future.wait(futures);
  }

  /// Deletes all documents of a specific type from Firestore.
  @override
  Future<void> all(Type type, { required bool iAmSure, String? subcollection }) async {
    if (iAmSure) {
      final String? className = LS.classNames[type.runtimeType];
      if (className == null) {
        throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
      }

      Map<String, dynamic>? snapshot;
      if (subcollection == null) {
        snapshot = await LS.instance.collection(className).get();
      }
      else {
        snapshot =
        await LS.instance.collection(className).doc(subcollection)
            .collection(subcollection)
            .get();
      }
      if (snapshot == null) return Future.value();
      if (snapshot.isEmpty) return Future.value();

      List<Future> futures = [];
      for (var entry in snapshot.entries) {
        futures.add(LS.instance.collection(className).doc(entry.key.split("/")[2]).delete());
      }

      await Future.wait(futures);
    }
  }

}