import 'package:cloud_firestore/cloud_firestore.dart';

import '../fs.dart';

class FSListDelegate {

  Future<List<T>> ofClass<T>(Type type, { int limit = 10 }) async {
    if (T.toString() != type.toString()) {
      throw ArgumentError("Type mismatch. Attempting to list items of type '${T.toString()}', but parameter type was ${type.toString()}");
    }

    final deserializer = FS.deserializers[type];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $type. Consider re-generating Firestorm data classes.');
    }

    final Query<Map<String, dynamic>> query = FS.firestore.collection(type.toString()).limit(limit);
    var querySnapshot = await query.get();
    List<T> objects = [];

    //TODO - Consider the use of Multithreading
    for (var doc in querySnapshot.docs) {
      objects.add(deserializer(doc.data()));
    }
    return objects;
  }

  Future<List<T>> allOfClass<T>(Type type) async {
    if (T.toString() != type.toString()) {
      throw ArgumentError("Type mismatch. Attempting to list items of type '${T.toString()}', but parameter type was ${type.toString()}");
    }

    final deserializer = FS.deserializers[type];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $type. Consider re-generating Firestorm data classes.');
    }

    final Query<Map<String, dynamic>> query = FS.firestore.collection(type.toString());
    var querySnapshot = await query.get();
    List<T> objects = [];

    //TODO - Consider the use of Multithreading
    for (var doc in querySnapshot.docs) {
      objects.add(deserializer(doc.data()));
    }
    return objects;
  }

  //TODO - Filter

}