

import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/element/type.dart';
import 'package:firestorm/gen/valid_class_holder.dart';
import 'package:firestorm/type/fs_types.dart';
import 'package:firestorm/type/rdb_types.dart';

import '../firestorm.dart';

/// A utility class to check Dart classes for specific criteria related to Firestorm objects.
class ClassChecker {

  ///Finds all classes annotated with @FirestormObject in the provided iterable of ClassElements.
  ///Returns a list of ClassElements that are annotated with @FirestormObject.
  static List<ClassElement> findAnnotatedClasses(final Iterable<ClassElement> classes) {
    List<ClassElement> annotatedClasses = [];
    for (final aClass in classes) {
      //Determine which classes are annotated with @FirestormObject
      if (aClass.metadata.any((m) => m.element?.displayName == 'FirestormObject')) {
        annotatedClasses.add(aClass);
      }
    }
    return annotatedClasses;
  }

  ///Finds all classes that have a public, no-argument constructor from a list of classes.
  ///Returns a list of ClassElements that meet the criteria.
  static List<ClassElement> findClassesWithPublicNoArgConstructor(final List<ClassElement> classes) {
    List<ClassElement> resultClasses = [];
    for (final aClass in classes) {
      //Check the class for a public no-argument constructor:
      if (aClass.constructors.isEmpty || !aClass.constructors.any((c) => c.isPublic && c.parameters.isEmpty)) {
        // ignore: avoid_print
        Firestorm.log.e("Annotated class ${aClass.name} ignored. It does not have a public no-argument constructor.");
      }
      else {
        resultClasses.add(aClass);
      }
    }
    return resultClasses;
  }

  ///Finds all classes that have an ID field of type String from a list of classes.
  ///Checks both the class itself and its superclasses for an ID field.
  ///Returns a list of ClassElements that meet the criteria.
  static List<ClassElement> findClassesWithIDField(final List<ClassElement> classes) {
    List<ClassElement> resultClasses = [];
    for (final aClass in classes) {
      //Check if the class or any of its superclasses has an ID field that is a String:
      bool hasIDField = false;
      for (final field in aClass.fields) {
        if (field.name == 'id' && field.type.element?.displayName == 'String') {
          hasIDField = true;
          break;
        }
      }
      for (final parent in aClass.allSupertypes) {
        for (final parentField in parent.element.fields) {
          if (parentField.name == 'id' && parentField.type.element?.displayName == 'String') {
            hasIDField = true;
            break;
          }
        }
      }

      if (!hasIDField) {
        // ignore: avoid_print
        Firestorm.log.e("Annotated class ${aClass.name} ignored. It (or its superclasses) does not have an ID field of type String.");
      }
      else {
        resultClasses.add(aClass);
      }
    }
    return resultClasses;
  }

  ///Checks if the classes have valid types for Firestore.
  static Set<ClassElement> checkClassesForValidFSTypes(final List<ClassElement> classes) {
    Set<ClassElement> resultClasses = {};
    for (final aClass in classes) {
      bool isValid = true;
      for (final field in aClass.fields) {
        if (!FSTypes.isTypeSupported(field.type)) {
          // ignore: avoid_print
          Firestorm.log.e("Annotated class ${aClass.name} ignored for Firestore. It has an unsupported type in field '${field.name}' for Firestore.");
          isValid = false;
          break;
        }
      }
      if (isValid) {
        resultClasses.add(aClass);
      }
    }
    return resultClasses;
  }

  ///Checks if the classes have valid RDB types.
  static Set<ClassElement> checkClassesForValidRDBTypes(final List<ClassElement> classes) {
    Set<ClassElement> resultClasses = {};
    for (final aClass in classes) {
      bool isValid = true;
      for (final field in aClass.fields) {
        if (!RDBTypes.isTypeSupported(field.type)) {
          // ignore: avoid_print
          Firestorm.log.e("Annotated class ${aClass.name} ignored for RDB. It has an unsupported type in field '${field.name}' for RDB.");
          isValid = false;
          break;
        }
      }
      if (isValid) {
        resultClasses.add(aClass);
      }
    }
    return resultClasses;
  }

  ///Filters the provided iterable of ClassElements to find valid classes for Firestorm.
  ///Returns a ValidClassHolder containing two lists: one for Firestore valid classes and one for RDB valid classes.
  static ValidClassHolder filter(final Iterable<ClassElement> classes) {
    //Find annotated classes:
    List<ClassElement> validClasses = findAnnotatedClasses(classes);

    //Filter out classes that do not have an ID field of type String:
    validClasses = findClassesWithIDField(validClasses);

    //Check classes for valid types:
    Set<ClassElement> fsValidClasses = checkClassesForValidFSTypes(validClasses);
    Set<ClassElement> rdbValidClasses = checkClassesForValidRDBTypes(validClasses);

    //Return a ValidClassHolder with the valid classes:
    return ValidClassHolder(fsValidClasses, rdbValidClasses);
  }

  /// Checks if the given DartType is an enum.
  static bool isEnumType(DartType type) {
    return type is InterfaceType &&
        type.element is EnumElement;
  }

}