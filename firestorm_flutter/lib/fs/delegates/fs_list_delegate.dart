import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/commons/delegate/list_delegate.dart';

import '../fs.dart';
import '../queries/fs_filterable.dart';

/// A delegate to list items.
class FSListDelegate implements ListDelegate {

  /// Lists a limited number of items of a specific type without a query.
  @override
  Future<List<T>> ofClass<T>(Type type, { int limit = 10, String? subcollection, GetOptions? getOptions }) async {
    if (T.toString() != type.toString()) {
      throw ArgumentError("Type mismatch. Attempting to list items of type '${T.toString()}', but parameter type was ${type.toString()}");
    }

    final deserializer = FS.deserializers[type];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $type. Consider re-generating Firestorm data classes.');
    }

    var collectionReference = FS.instance.collection(type.toString());
    if (subcollection != null) {
      collectionReference = collectionReference.doc(subcollection).collection(subcollection);
    }

    final Query<Map<String, dynamic>> query = collectionReference.limit(limit);
    var querySnapshot = await query.get(getOptions);
    List<T> objects = [];

    //TODO - Consider the use of Multithreading
    for (var doc in querySnapshot.docs) {
      if (doc.exists) {
        objects.add(deserializer(doc.data()));
      }
    }
    return objects;
  }

  /// Lists all items of a specific type.
  @override
  Future<List<T>> allOfClass<T>(Type type, { String? subcollection, GetOptions? getOptions }) async {
    if (T.toString() != type.toString()) {
      throw ArgumentError("Type mismatch. Attempting to list items of type '${T.toString()}', but parameter type was ${type.toString()}");
    }

    final deserializer = FS.deserializers[type];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $type. Consider re-generating Firestorm data classes.');
    }

    var collectionReference = FS.instance.collection(type.toString());
    if (subcollection != null) {
      collectionReference = collectionReference.doc(subcollection).collection(subcollection);
    }
    final Query<Map<String, dynamic>> query = collectionReference;
    var querySnapshot = await query.get(getOptions);
    List<T> objects = [];

    //TODO - Consider the use of Multithreading
    for (var doc in querySnapshot.docs) {
      if (doc.exists) {
        objects.add(deserializer(doc.data()));
      }
    }
    return objects;
  }

  /// Applies a filter to a specific type of items and returns a list of items.
  FSFilterable<T> filter<T>(Type type, { String? subcollection, GetOptions? getOptions }) {
    var collectionReference = FS.instance.collection(type.toString());
    if (subcollection != null) {
      collectionReference = collectionReference.doc(subcollection).collection(subcollection);
    }
    return FSFilterable<T>(collectionReference, type, getOptions: getOptions);
  }

}