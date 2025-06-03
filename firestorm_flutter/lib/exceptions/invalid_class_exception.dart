class InvalidClassException implements Exception {

  final String className;

  //To be thrown when a class is not a valid Firestorm class.
  InvalidClassException(this.className);

  String get message => 'InvalidClassException: $className is not a valid Firestorm class.'
      'Please ensure that the class is annotated with @FirestormObject(), has a public no-argument constructor, and an ID field.';

  @override
  String toString() => message;

}