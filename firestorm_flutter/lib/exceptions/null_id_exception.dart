class NullIDException implements Exception {

  final dynamic object;

  // To be thrown when an object has a null or empty ID field.
  NullIDException(this.object);

  String get message => 'NullIDException: $object';

  @override
  String toString() => message;

}