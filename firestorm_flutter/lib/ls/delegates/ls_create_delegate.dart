import 'package:localstore/localstore.dart';

import '../../commons/delegate/create_delegate.dart';
import '../../exceptions/null_id_exception.dart';
import '../ls.dart';

/// A delegate class to create documents in Localstore.
class LSCreateDelegate implements CreateDelegate {

  /// Creates a document in Localstore from the given object.
  @override
  Future<void> one(dynamic object, { String? subcollection }) {
    final serializer = LS.serializers[object.runtimeType];
    final String? className = LS.classNames[object.runtimeType];
    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    String id = map["id"];
    if (id.isEmpty) {
      throw NullIDException(map);
    }
    DocumentRef ref = LS.instance.collection(className).doc(id);
    if (subcollection != null) {
      ref = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(id);
    }
    return ref.set(map);
  }

  /// Creates multiple documents in Localstore from a list of objects.
  /// Uses a batch operation for efficiency.
  @override
  Future<void> many<T>(List<T> objects, { String? subcollection }) async {
    if (objects.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
    }
    if (objects.isEmpty) {
      return Future.value();
    }

    final serializer = LS.serializers[objects[0].runtimeType];
    final String? className = LS.classNames[objects[0].runtimeType];

    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: ${objects[0].runtimeType}. Consider re-generating Firestorm data classes.');
    }

    List<Future> futures = [];

    for (var object in objects) {

      final map = serializer(object);
      if (map["id"].isEmpty) {
        throw NullIDException(map);
      }

      DocumentRef documentReference = LS.instance.collection(className).doc(map["id"]);
      if (subcollection != null) {
        documentReference = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(map["id"]);
      }

      futures.add(documentReference.set(serializer(object)));
    }

    await Future.wait(futures);
  }

}