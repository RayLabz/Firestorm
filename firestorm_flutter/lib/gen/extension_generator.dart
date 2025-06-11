import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/element/nullability_suffix.dart';
import 'package:analyzer/dart/element/type.dart';
import 'package:firestorm/exceptions/invalid_class_exception.dart';

/// Generates a Dart extension for the given class name.
class ExtensionGenerator {

  /// Generates a Dart extension for the given class name.
  static String generateExtension(
      final StringBuffer classBuffer,
      final ClassElement aClass,
      final bool hasFSSupport,
      final bool hasRDBSupport
      ) {
    classBuffer.writeln("// - - - - - - - FirestormObject ${aClass.name} - - - - - - -");
    classBuffer.writeln();

    //Generate extension class
    classBuffer.writeln("extension ${aClass.name}Model on ${aClass.name} {");

    classBuffer.writeln();

    //Generate static fields for Firestore & RDB support
    if (hasFSSupport) {
      classBuffer.writeln("\tstatic final bool fsSupport = true;");
    }
    else {
      classBuffer.writeln("\tstatic final bool fsSupport = false;");
    }

    if (hasRDBSupport) {
      classBuffer.writeln("\tstatic final bool rdbSupport = true;");
    }
    else {
      classBuffer.writeln("\tstatic final bool rdbSupport = false;");
    }
    classBuffer.writeln();

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
          //Do nothing, this is kept for reference.
          // classBuffer.writeln("\t\t\t '${param.name}': null,"); //set excluded to null
        }
        else {
          throw InvalidClassException(aClass.name); //cannot have excluded without nullable
        }
      }
      else {
        //If this is a user-defined type, expand it using it own toMap():
        if (!matchingField.type.element!.library!.isDartCore && matchingField.type is InterfaceType) {
          classBuffer.writeln("\t\t\t '${param.name}': this.${param.name}.toMap(),"); //call toMap() on user-defined type
        }
        else {
          //Otherwise, just use the attribute:
          classBuffer.writeln("\t\t\t '${param.name}': this.${param.name},"); //not excluded (normal)
        }
      }
    }

    classBuffer.writeln("\t\t };");
    classBuffer.writeln("\t }");
    classBuffer.writeln();

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
        //If this is a user-defined type, use its own fromMap():
        if (!matchingField.type.element!.library!.isDartCore && matchingField.type is InterfaceType) {
          classBuffer.writeln("\t\t\t ${matchingField.type.getDisplayString()}Model.fromMap(Map<String, dynamic>.from(map['${param.name}'] as Map)),"); //call fromMap() on user-defined type
        }
        else {
          if (param.type.isDartCoreList) {
            final listType = param.type as InterfaceType;
            DartType elementType = listType.typeArguments[0];
            classBuffer.writeln("\t\t\t map['${param.name}'].cast<${elementType.getDisplayString()}>(),"); //cast to List<elementType>
          }
          else if (param.type.isDartCoreMap) {
            final listType = param.type as InterfaceType;
            DartType valueType = listType.typeArguments[1];
            classBuffer.writeln("\t\t\t map['${param.name}'].cast<String, ${valueType.getDisplayString()}>(),"); //cast to Map<String, valueType>
          }
          else {
            classBuffer.writeln("\t\t\t map['${param.name}'] as ${matchingField.type.getDisplayString()},"); //not excluded (normal)
          }
        }
      }
    }

    classBuffer.writeln("\t\t );");
    classBuffer.writeln("\t }");
    classBuffer.writeln();

    //End extension class
    classBuffer.writeln("}");
    classBuffer.writeln();

    return classBuffer.toString();
  }
}