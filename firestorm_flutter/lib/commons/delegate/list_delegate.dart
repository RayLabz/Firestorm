/// Contains the operations for list delegates.
abstract class ListDelegate {

  /// Lists a limited number of items of a specific type without a query.
  Future<List<T>> ofClass<T>(Type type, { int limit = 10, String? subcollection });

  /// Lists all items of a specific type.
  Future<List<T>> allOfClass<T>(Type type, { String? subcollection });

}