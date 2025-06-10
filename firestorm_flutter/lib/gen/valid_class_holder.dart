import 'package:analyzer/dart/element/element.dart';

/// A holder for valid classes that can be used in Firestorm.
class ValidClassHolder {

  final Set<ClassElement> fsValidClasses;
  final Set<ClassElement> rdbValidClasses;

  ValidClassHolder(this.fsValidClasses, this.rdbValidClasses);

  ValidClassHolder.empty() : fsValidClasses = {}, rdbValidClasses = {};

  /// Returns a list of all valid classes.
  Set<ClassElement> getAllValidClasses() {
    return rdbValidClasses.union(fsValidClasses);
  }

  /// Checks if a class has Firestore support.
  bool hasFSSupport(ClassElement classElement) {
    return fsValidClasses.contains(classElement);
  }

  /// Checks if a class has Realtime Database support.
  bool hasRDBSupport(ClassElement classElement) {
    return rdbValidClasses.contains(classElement);
  }

  /// Joins another ValidClassHolder into this one.
  join(ValidClassHolder other) {
    rdbValidClasses.addAll(other.rdbValidClasses);
    fsValidClasses.addAll(other.fsValidClasses);
  }

}