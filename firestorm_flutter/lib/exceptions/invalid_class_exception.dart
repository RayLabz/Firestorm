class InvalidClassException implements Exception {

  final String className;

  //To be thrown when a class is not a valid Firestorm class.
  InvalidClassException(this.className);

  String get message => """InvalidClassException: $className is not a valid Firestorm class. 
  Please ensure that the class is annotated with @FirestormObject(), has an ID field, and that any excluded fields are also nullable.""";

  @override
  String toString() => message;

}