/// A type that helps with filtering objects from database queries.
abstract class Filterable<QueryType> {

  QueryType query;

  final Type type;

  /// Creates a new filterable.
  Filterable(this.query, this.type);

}