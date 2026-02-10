import 'package:firestorm/commons/delegate/list_delegate.dart';

import '../ls.dart';

/// A delegate to list items.
class LSListDelegate implements ListDelegate {

  /// Lists a limited number of items of a specific type without a query.
  @override
  Future<List<T>> ofClass<T>(Type type, { int limit = 10, String? subcollection }) async {
    if (T.toString() != type.toString()) {
      throw ArgumentError("Type mismatch. Attempting to list items of type '${T.toString()}', but parameter type was ${type.toString()}");
    }

    final deserializer = LS.deserializers[type];
    final String? className = LS.classNames[type];
    if (deserializer == null || className == null) {
      throw UnsupportedError('No deserializer/class name found for type: $className. Consider re-generating Firestorm data classes.');
    }

    var collectionReference = LS.instance.collection(className);
    if (subcollection != null) {
      collectionReference = collectionReference.doc(subcollection).collection(subcollection);
    }

    Map<String, dynamic>? items = await collectionReference.get();
    if (items == null) {
      return [];
    }

    List<T> objects = [];

    //TODO - Consider the use of Multithreading
    for (var doc in items.entries) {
      if (doc.value != null) {
        objects.add(deserializer(doc.value));
      }
      if (objects.length >= limit) {  //Manually stops the reading.
        break;
      }
    }
    return objects;
  }

  /// Lists all items of a specific type.
  @override
  Future<List<T>> allOfClass<T>(Type type, { String? subcollection }) async {
    if (T.toString() != type.toString()) {
      throw ArgumentError("Type mismatch. Attempting to list items of type '${T.toString()}', but parameter type was ${type.toString()}");
    }

    final deserializer = LS.deserializers[type];
    final String? className = LS.classNames[type];
    if (deserializer == null || className == null) {
      throw UnsupportedError('No deserializer/class name found for type: $className. Consider re-generating Firestorm data classes.');
    }

    var collectionReference = LS.instance.collection(className);
    if (subcollection != null) {
      collectionReference = collectionReference.doc(subcollection).collection(subcollection);
    }

    Map<String, dynamic>? items = await collectionReference.get();
    if (items == null) {
      return [];
    }

    List<T> objects = [];

    //TODO - Consider the use of Multithreading
    for (var doc in items.entries) {
      if (doc.value != null) {
        objects.add(deserializer(doc.value));
      }
    }
    return objects;
  }

}