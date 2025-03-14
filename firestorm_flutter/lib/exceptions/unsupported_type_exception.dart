class UnsupportedTypeException implements Exception {

  final dynamic value;

  UnsupportedTypeException(this.value);

  String get message => 'UnsupportedTypeException: ${value.runtimeType}';

  @override
  String toString() => 'UnsupportedTypeException: $message';

}