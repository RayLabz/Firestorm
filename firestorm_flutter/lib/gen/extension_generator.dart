import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/element/nullability_suffix.dart';
import 'package:firestorm/exceptions/invalid_class_exception.dart';

/// Generates a Dart extension for the given class name.
class ExtensionGenerator {

  /// Generates a Dart extension for the given class name.
  static String generateExtension(final StringBuffer classBuffer, final ClassElement aClass) {
    classBuffer.writeln("// - - - - - - - FirestormObject ${aClass.name} - - - - - - -");
    classBuffer.writeln();

    //Generate extension class
    classBuffer.writeln("extension ${aClass.name}Model on ${aClass.name} {");

    //Generate toMap() method
    classBuffer.writeln("\t Map<String, dynamic> toMap() {");
    classBuffer.writeln("\t\t return {");

    ConstructorElement constructorElement = aClass.unnamedConstructor!;
    List<ParameterElement> params = constructorElement.parameters;

    //Fields:
    for (ParameterElement param in params) {
      FieldElement? matchingField = aClass.getField(param.displayName); //match to field

      for (final parent in aClass.allSupertypes) { //Find field in parent classes
        for (final parentField in parent.element.fields) {
          if (parentField.name != "hashCode" &&
              parentField.name == param.name &&
              parentField.name != "runtimeType") {
            matchingField = parentField; //set matching field to parent field
            break;
          }
        }
      }

      if (matchingField == null) {
        throw InvalidClassException(aClass.name);
      }

      if (matchingField.metadata.any((m) => m.element?.displayName == 'Exclude')) {
        if (matchingField.type.nullabilitySuffix == NullabilitySuffix.question) {
          classBuffer.writeln("\t\t\t '${param.name}': null,"); //set excluded to null
        }
        else {
          throw InvalidClassException(aClass.name); //cannot have excluded without nullable
        }
      }
      else {
        classBuffer.writeln("\t\t\t '${param.name}': this.${param.name},"); //not excluded (normal)
      }
    }

    classBuffer.writeln("\t\t };");
    classBuffer.writeln("\t }");

    //Generate fromMap() method
    classBuffer.writeln("\tstatic ${aClass.name} fromMap(Map<String, dynamic> map) {");
    classBuffer.writeln("\t\t return ${aClass.name}(");

    //Constructor parameters:
    for (ParameterElement param in params) {

      FieldElement? matchingField = aClass.getField(param.displayName); //match to field

      for (final parent in aClass.allSupertypes) { //Find field in parent classes
        for (final parentField in parent.element.fields) {
          if (parentField.name != "hashCode" &&
              parentField.name == param.name &&
              parentField.name != "runtimeType") {
            matchingField = parentField; //set matching field to parent field
            break;
          }
        }
      }

      if (matchingField == null) {
        throw InvalidClassException(aClass.name);
      }

      if (matchingField.metadata.any((m) => m.element?.displayName == 'Exclude')) {
        if (matchingField.type.nullabilitySuffix == NullabilitySuffix.question) {
          classBuffer.writeln("\t\t\t null,"); //set excluded to null
        }
        else {
          throw InvalidClassException(aClass.name); //cannot have excluded without nullable
        }
      }
      else {
        //TODO - Type conversions, e.g. toDouble, toInt etc.
        classBuffer.writeln("\t\t\t map['${param.name}'],"); //not excluded (normal)
      }
    }

    classBuffer.writeln("\t\t );");
    classBuffer.writeln("\t }");

    //End extension class
    classBuffer.writeln("}");
    classBuffer.writeln();

    return classBuffer.toString();
  }
}