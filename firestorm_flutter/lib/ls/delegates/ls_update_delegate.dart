import 'package:firestorm/commons/delegate/update_delegate.dart';
import 'package:localstore/localstore.dart';

import '../../exceptions/null_id_exception.dart';
import '../ls.dart';

/// A delegate class to updates documents on Localstore.
class LSUpdateDelegate implements UpdateDelegate {

  /// Updates a document in Localstore with the given object.
  @override
  Future<void> one<T>(T object, { String? subcollection }) {
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
    return ref.set(map);
  }

  /// Updates multiple documents in Localstore with a list of objects.
  @override
  Future<void> many<T>(List<T> objects, { String? subcollection }) async {
    if (objects.isEmpty) return Future.value();
    if (objects.length > 500) {
      throw ArgumentError('Batch limit exceeded. Maximum 500 objects allowed.');
    }
    final serializer = LS.serializers[objects[0].runtimeType];
    final String? className = LS.classNames[objects[0].runtimeType];
    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: $className. Consider re-generating Firestorm data classes.');
    }

    List<Future> futures = [];

    for (var object in objects) {
      if (object.runtimeType != T) {
        throw ArgumentError('All objects must be of type $T');
      }
      final map = serializer(object);
      if (map["id"].isEmpty) {
        throw NullIDException(map);
      }
      DocumentRef ref = LS.instance.collection(className).doc(map["id"]);
      if (subcollection != null) {
        ref = LS.instance.collection(className).doc(subcollection).collection(subcollection).doc(map["id"]);
      }

      futures.add(ref.set(map));
    }
    await Future.wait(futures);
  }

}