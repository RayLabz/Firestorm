/// Contains the operations for update delegates.
abstract class UpdateDelegate {

  /// Updates a single object in the database.
  Future<void> one<T>(T object, { String? subcollection });

  /// Updates multiple objects in the database.
  Future<void> many<T>(List<T> objects, { String? subcollection });

}