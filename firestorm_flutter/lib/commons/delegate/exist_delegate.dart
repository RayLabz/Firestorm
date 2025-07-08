/// Contains the operations for exist delegates.
abstract class ExistDelegate {

  /// Checks if an object exists in the database.
  Future<bool> one<T>(dynamic object, { String? subcollection });

  /// Checks if a document exists in the database using its type and ID.
  Future<bool> oneWithID<T>(Type type, String documentID, { String? subcollection });

}