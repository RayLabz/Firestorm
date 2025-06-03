class UnsupportedTypeException implements Exception {

  final dynamic value;

  //To be thrown when a value is not a supported Firestore/RDB type.
  UnsupportedTypeException(this.value);

  String get message => 'UnsupportedTypeException: ${value.runtimeType}';

  @override
  String toString() => message;

}