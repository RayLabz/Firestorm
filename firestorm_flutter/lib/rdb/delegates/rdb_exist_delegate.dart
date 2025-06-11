import '../rdb.dart';

/// A delegate class to check if a document exists in the RDB.
class RDBExistDelegate {

  /// Checks if a document exists in RDB.
  Future<bool> one<T>(Type type, String documentID, { String? subcollection }) async {
    String path = RDB.constructPathForClassAndID(type, documentID, subcollection: subcollection);
    final snapshot = await RDB.rdb.ref(path).get();
    return snapshot.exists;
  }

}