/// A type that helps with filtering objects from database queries.
abstract class Filterable<QueryType, ObjectType> {

  QueryType _query;

  final ObjectType _type;

  /// Creates a new filterable.
  Filterable(this._query, this._type);

  ObjectType get type => _type;

  QueryType get query => _query;

  set query(QueryType value) {
    _query = value;
  }

}