import 'package:firestorm/commons/delegate/exist_delegate.dart';

import '../rdb.dart';

/// A delegate class to check if a document exists in the RDB.
class RDBExistDelegate implements ExistDelegate{

  /// Checks if a document exists in the RDB.
  @override
  Future<bool> one<T>(dynamic object, { String? subcollection }) async {
    final String? className = RDB.classNames[object.runtimeType];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $className. Consider re-generating Firestorm data classes.');
    }
    if (object.id == null) {
      return false;
    }
    String path = RDB.constructPathForClassAndID(className, object.id, subcollection: subcollection);
    final snapshot = await RDB.instance.ref(path).get();
    return snapshot.exists;
  }

  /// Checks if a document exists in the RDB using its type and ID.
  @override
  Future<bool> oneWithID<T>(Type type, String documentID, { String? subcollection }) async {
    final String? className = RDB.classNames[type];
    if (className == null) {
      throw UnsupportedError('No class name found for type: $type. Consider re-generating Firestorm data classes.');
    }
    String path = RDB.constructPathForClassAndID(className, documentID, subcollection: subcollection);
    final snapshot = await RDB.instance.ref(path).get();
    return snapshot.exists;
  }

}