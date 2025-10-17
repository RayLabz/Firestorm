import 'package:firestorm/commons/delegate/get_delegate.dart';
import 'package:localstore/localstore.dart';

import '../ls.dart';

/// A delegate class to read documents from Localstore.
class LSGetDelegate implements GetDelegate {

  /// Reads a document from Localstore and converts it to the specified type.
  @override
  Future<T?> one<T>(String documentID, { String? subcollection }) async {
    final deserializer = LS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    DocumentRef ref = LS.instance.collection(T.toString()).doc(documentID);
    if (subcollection != null) {
      ref = LS.instance.collection(T.toString()).doc(subcollection).collection(subcollection).doc(documentID);
    }

    Map<String, dynamic>? snapshot = await ref.get();
    if (snapshot == null) {
      return null;
    }
    T object = deserializer(snapshot) as T;
    return object;
  }

  /// Reads multiple documents from Localstore and converts them to a list of the specified type.
  /// Sends multiple requests at the same time and returns when all are completed.
  @override
  Future<List<T>> many<T>(List<String> documentIDs, { String? subcollection }) async {
    final deserializer = LS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    List<T> objects = [];
    List<DocumentRef> refs;
    if (subcollection != null) {
      refs = documentIDs.map((id) => LS.instance.collection(T.toString()).doc(subcollection).collection(subcollection).doc(id)).toList();
    }
    else {
      refs = documentIDs.map((id) => LS.instance.collection(T.toString()).doc(id)).toList();
    }
    List<Map<String, dynamic>?> snapshots = await Future.wait(refs.map((ref) => ref.get()));
    for (var snapshot in snapshots) {
      if (snapshot != null) {
        T object = deserializer(snapshot) as T;
        objects.add(object);
      }
    }
    return objects;
  }


}