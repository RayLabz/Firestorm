/// Contains the operations for delete delegates.
abstract class DeleteDelegate {

  /// Deletes a single document from the database using the provided object.
  Future<void> one(dynamic object, { String? subcollection });

  /// Deletes multiple documents from the database using a list of objects.
  Future<void> many<T>(List<T> objects, { String? subcollection });

  /// Deletes a document from the database by its type and document ID.
  Future<void> oneWithID(Type type, String documentID, { String? subcollection });

  /// Deletes multiple documents from the database by their type and a list of document IDs.
  Future<void> manyWithIDs(Type type, List<String> documentIDs, { String? subcollection });

  /// Deletes all documents of a specific type from the database.
  Future<void> all(Type type, { required bool iAmSure, String? subcollection });

}