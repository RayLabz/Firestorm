import '../rdb.dart';

/// A delegate class to check if a document exists in the RDB.
class RDBExistDelegate {

  /// Checks if a document exists in the RDB.
  Future<bool> one<T>(dynamic object, { String? subcollection }) async {
    if (object.id == null) {
      return false;
    }
    String path = RDB.constructPathForClassAndID(object.runtimeType, object.id, subcollection: subcollection);
    final snapshot = await RDB.rdb.ref(path).get();
    return snapshot.exists;
  }

  /// Checks if a document exists in the RDB using its type and ID.
  Future<bool> oneWithID<T>(Type type, String documentID, { String? subcollection }) async {
    String path = RDB.constructPathForClassAndID(type, documentID, subcollection: subcollection);
    final snapshot = await RDB.rdb.ref(path).get();
    return snapshot.exists;
  }

}