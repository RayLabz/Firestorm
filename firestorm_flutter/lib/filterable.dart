/// A type that helps with filtering objects from database queries.
abstract class Filterable<QueryType> {

  QueryType _query;

  final Type _type;

  /// Creates a new filterable.
  Filterable(this._query, this._type);

  Type get type => _type;

  QueryType get query => _query;

  set query(QueryType value) {
    _query = value;
  }

}