import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/rdb/helpers/rdb_deserialization_helper.dart';

import '../rdb.dart';

/// A delegate class to read documents from Firestore.
class RDBGetDelegate {

  /// Reads a document from Firestore and converts it to the specified type.
  Future<T?> one<T>(String documentID, { String? subcollection }) async {
    final deserializer = RDB.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    String path = RDB.constructPathForClassAndID(T, documentID, subcollection: subcollection);
    DataSnapshot snapshot = await RDB.instance.ref(path).get();
    if (snapshot.exists) {
      final Map<String, dynamic> data = RDBDeserializationHelper.snapshotToMap(snapshot);
      return deserializer(data) as T;
    }
    return null;
  }

  /// Reads multiple documents from Firestore and converts them to a list of the specified type.
  Future<List<T>> many<T>(List<String> documentIDs, { String? subcollection }) async {
    final deserializer = RDB.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    List<T> objects = [];
    List<String> paths = [];

    for (String documentID in documentIDs) {
      String path = RDB.constructPathForClassAndID(T, documentID, subcollection: subcollection);
      paths.add(path);
    }

    //Convert paths to a list of futures, process simultaneously, and wait for all to complete:
    Iterable<Future<dynamic>> futures = documentIDs.map((documentID) => one<T>(documentID, subcollection: subcollection));

    List<dynamic> list = await Future.wait(futures);
    for (var object in list) {
      if (object != null) {
        if (object is T) {
          objects.add(object);
        } else {
          throw UnsupportedError(
              'Expected type $T but got ${object.runtimeType}');
        }
      }
    }
    return objects;
  }


}