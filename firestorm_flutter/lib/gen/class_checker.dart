import 'package:analyzer/dart/element/element.dart';
import 'package:colorful_text/colorful_text.dart';
import 'package:firestorm/type/fs_types.dart';

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
        print(ColorfulText.paint("Annotated class ${aClass.name} ignored. It does not have a public no-argument constructor.", ColorfulText.red));
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
        print(ColorfulText.paint("Annotated class ${aClass.name} ignored. It (or its superclasses) does not have an ID field of type String.", ColorfulText.red));
      }
      else {
        resultClasses.add(aClass);
      }
    }
    return resultClasses;
  }

  static List<ClassElement> checkClassesForValidTypes(final List<ClassElement> classes) {
    List<ClassElement> resultClasses = [];
    for (final aClass in classes) {
      bool isValid = true;
      for (final field in aClass.fields) {
        if (!FSTypes.isTypeSupported(field.type)) {
          print(ColorfulText.paint("Annotated class ${aClass.name} ignored. It has an unsupported type in field '${field.name}'.", ColorfulText.red));
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

  static List<ClassElement> filter(final Iterable<ClassElement> classes) {
    //Find annotated classes:
    List<ClassElement> validClasses = findAnnotatedClasses(classes);

    //Filter out classes that do not have a public no-argument constructor:
    // annotatedClasses = findClassesWithPublicNoArgConstructor(annotatedClasses);

    //Filter out classes that do not have an ID field of type String:
    validClasses = findClassesWithIDField(validClasses);

    //Check classes for valid types:
    validClasses = checkClassesForValidTypes(validClasses);

    return validClasses;
  }

}