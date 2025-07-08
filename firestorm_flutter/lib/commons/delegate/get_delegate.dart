/// Contains the operations for get delegates.
abstract class GetDelegate {

  /// Reads a document from the database and converts it to the specified type.
  Future<T?> one<T>(String documentID, { String? subcollection });

  /// Reads multiple documents from the database and converts them to a list of the specified type.
  Future<List<T>> many<T>(List<String> documentIDs, { String? subcollection });

}