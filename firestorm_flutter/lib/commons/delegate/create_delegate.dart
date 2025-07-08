/// Contains the operations for create delegates.
abstract class CreateDelegate {

  /// Creates a new object in the database.
  Future<void> one(dynamic object, { String? subcollection });

  /// Creates multiple objects in the database.
  Future<void> many<T>(List<T> objects, { String? subcollection });

}