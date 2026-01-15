import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/element/nullability_suffix.dart';
import 'package:analyzer/dart/element/type.dart';
import 'package:firestorm/exceptions/invalid_class_exception.dart';

import 'class_checker.dart';

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

    if (aClass.unnamedConstructor == null) {
      throw InvalidClassException(aClass.name);
    }

    final ConstructorElement constructorElement = aClass.unnamedConstructor!;
    final List<ParameterElement> constructorParams = constructorElement.parameters;

    _generateStaticFields(classBuffer, hasFSSupport, hasRDBSupport);
    _generateToMap(classBuffer, aClass);
    _generateFromMap(classBuffer, aClass, constructorParams);
    return classBuffer.toString();
  }

  /// Generates static fields.
  static void _generateStaticFields(final StringBuffer classBuffer, final bool hasFSSupport, final bool hasRDBSupport) {
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
  }

  /// Generates the toMap() method.
  static void _generateToMap(final StringBuffer classBuffer, final ClassElement aClass) {

    List<FieldElement> allFields = _findAllFieldsForClass(aClass);

    //Generate toMap() method
    classBuffer.writeln("\t Map<String, dynamic> toMap() {");
    classBuffer.writeln("\t\t return {");

    //Fields:
    for (final field in allFields) {
      // FieldElement? matchingField = field; //match to field

      // for (final parent in aClass.allSupertypes) { //Find field in parent classes
      //   for (final parentField in parent.element.fields) {
      //     if (parentField.name != "hashCode" &&
      //         parentField.name == field.name &&
      //         parentField.name != "runtimeType") {
      //       matchingField = parentField; //set matching field to parent field
      //       break;
      //     }
      //   }
      // }

      // if (matchingField == null) {
      //   throw InvalidClassException(aClass.name);
      // }

      if (field.metadata.any((m) => m.element?.displayName == 'Exclude')) {
        if (field.type.nullabilitySuffix == NullabilitySuffix.question) {
          //Do nothing, this is kept for reference.
          // classBuffer.writeln("\t\t\t '${param.name}': null,"); //set excluded to null
        }
        else {
          throw InvalidClassException(aClass.name); //cannot have excluded without nullable
        }
      }
      else {
        //If this is a user-defined type, expand it using it own toMap():
        if (!field.type.element!.library!.isDartCore && field.type is InterfaceType && !ClassChecker.isEnumType(field.type)) {
          classBuffer.writeln("\t\t\t '${field.name}': this.${field.name}.toMap(),"); //call toMap() on user-defined type
        }
        //enum:
        else if (ClassChecker.isEnumType(field.type)) {
          classBuffer.writeln("\t\t\t '${field.name}': this.${field.name}.toString(),"); //not excluded (normal, enum)
        }
        //other:
        else {
          //Otherwise, just use the attribute:
          classBuffer.writeln("\t\t\t '${field.name}': this.${field.name},"); //not excluded (normal)
        }
      }
    }

    classBuffer.writeln("\t\t };");
    classBuffer.writeln("\t }");
    classBuffer.writeln();
  }

  /// Generates the fromMap() method.
  static void _generateFromMap(final StringBuffer classBuffer, final ClassElement aClass, final List<ParameterElement> constructorParams) {
    //Generate fromMap() method
    classBuffer.writeln("\tstatic ${aClass.name} fromMap(Map<String, dynamic> map) {");
    classBuffer.writeln("\t\t${aClass.name} object = ${aClass.name}(");

    List<String> constructorParamNames = [];

    // Positional parameters first:
    for (ParameterElement param in constructorParams.where((p) => p.isPositional)) {
      FieldElement? matchingField = aClass.getField(param.displayName); // match to field
      constructorParamNames.add(param.name);

      for (final parent in aClass.allSupertypes) { // find field in parent classes
        for (final parentField in parent.element.fields) {
          if (parentField.name != "hashCode" &&
              parentField.name == param.name &&
              parentField.name != "runtimeType") {
            matchingField = parentField;
            break;
          }
        }
      }

      if (matchingField == null) {
        throw InvalidClassException(aClass.name);
      }

      if (matchingField.metadata.any((m) => m.element?.displayName == 'Exclude')) {
        if (matchingField.type.nullabilitySuffix == NullabilitySuffix.question) {
          classBuffer.writeln("\t\t\tnull,"); // set excluded to null
        } else {
          throw InvalidClassException(aClass.name); // cannot have excluded without nullable
        }
      } else {
        // If this is a user-defined type, use its own fromMap():
        if (!matchingField.type.element!.library!.isDartCore &&
            matchingField.type is InterfaceType &&
            !ClassChecker.isEnumType(matchingField.type)) {
          classBuffer.writeln(
              "\t\t\t${matchingField.type.getDisplayString()}Model.fromMap(Map<String, dynamic>.from(map['${param.name}'] as Map)),");
        }
        // List
        else if (param.type.isDartCoreList) {
          final listType = param.type as InterfaceType;
          DartType elementType = listType.typeArguments[0];
          classBuffer.writeln(
              "\t\t\tmap['${param.name}'] != null ? map['${param.name}'].cast<${elementType.getDisplayString()}>() : [],");
        }
        // Map
        else if (param.type.isDartCoreMap) {
          final listType = param.type as InterfaceType;
          DartType valueType = listType.typeArguments[1];
          classBuffer.writeln(
              "\t\t\tmap['${param.name}'] != null ? map['${param.name}'].cast<String, ${valueType.getDisplayString()}>() : {},");
        }
        // Enum
        else if (ClassChecker.isEnumType(param.type)) {
          classBuffer.writeln(
              "\t\t\t${param.type.getDisplayString()}.values.firstWhere((e) => e.toString() == map['${param.name}'] as String),");
        }
        // Other types
        else {
          classBuffer.writeln(
              "\t\t\tmap['${param.name}'] as ${matchingField.type.getDisplayString()},");
        }
      }
    }

    // Then named parameters in a map:
    if (constructorParams.any((p) => p.isNamed)) {
      // classBuffer.writeln("\t\t\t{");
      for (ParameterElement param in constructorParams.where((p) => p.isNamed)) {
        FieldElement? matchingField = aClass.getField(param.displayName); // match to field
        constructorParamNames.add(param.name);

        for (final parent in aClass.allSupertypes) { // find field in parent classes
          for (final parentField in parent.element.fields) {
            if (parentField.name != "hashCode" &&
                parentField.name == param.name &&
                parentField.name != "runtimeType") {
              matchingField = parentField;
              break;
            }
          }
        }

        if (matchingField == null) {
          throw InvalidClassException(aClass.name);
        }

        if (matchingField.metadata.any((m) => m.element?.displayName == 'Exclude')) {
          if (matchingField.type.nullabilitySuffix == NullabilitySuffix.question) {
            classBuffer.writeln("\t\t\t${param.name}: null,"); // set excluded to null
          } else {
            throw InvalidClassException(aClass.name); // cannot have excluded without nullable
          }
        } else {
          // If this is a user-defined type, use its own fromMap():
          if (!matchingField.type.element!.library!.isDartCore &&
              matchingField.type is InterfaceType &&
              !ClassChecker.isEnumType(matchingField.type)) {
            classBuffer.writeln(
                "\t\t\t${param.name}: ${matchingField.type.getDisplayString()}Model.fromMap(Map<String, dynamic>.from(map['${param.name}'] as Map)),");
          }
          // List
          else if (param.type.isDartCoreList) {
            final listType = param.type as InterfaceType;
            DartType elementType = listType.typeArguments[0];
            classBuffer.writeln(
                "\t\t\t${param.name}: map['${param.name}'] != null ? map['${param.name}'].cast<${elementType.getDisplayString()}>() : [],");
          }
          // Map
          else if (param.type.isDartCoreMap) {
            final listType = param.type as InterfaceType;
            DartType valueType = listType.typeArguments[1];
            classBuffer.writeln(
                "\t\t\t${param.name}: map['${param.name}'] != null ? map['${param.name}'].cast<String, ${valueType.getDisplayString()}>() : {},");
          }
          // Enum
          else if (ClassChecker.isEnumType(param.type)) {
            classBuffer.writeln(
                "\t\t\t${param.name}: ${param.type.getDisplayString()}.values.firstWhere((e) => e.toString() == map['${param.name}'] as String),");
          }
          // Other types
          else {
            classBuffer.writeln(
                "\t\t\t${param.name}: map['${param.name}'] as ${matchingField.type.getDisplayString()},");
          }
        }
      }
      // classBuffer.writeln("\t\t\t}");
    }

    classBuffer.writeln("\t\t );");

    //Set values for parameters that are not in the constructor:
    final List<FieldElement> allFields = _findAllFieldsForClass(aClass);

    for (final field in allFields) {

      if (constructorParamNames.contains(field.name)) {
        continue; // skip fields that are already in the constructor
      }

      //Fields NOT in the constructor:
      if (field.metadata.any((m) => m.element?.displayName == 'Exclude')) {
        if (field.type.nullabilitySuffix == NullabilitySuffix.question) {
          classBuffer.writeln("\t\t\tobject.${field.name} = null;"); // set excluded to null
        } else {
          throw InvalidClassException(aClass.name); // cannot have excluded without nullable
        }
      } else {
        // If this is a user-defined type, use its own fromMap():
        if (!field.type.element!.library!.isDartCore &&
            field.type is InterfaceType &&
            !ClassChecker.isEnumType(field.type)) {
          classBuffer.writeln(
              "\t\t\tobject.${field.name} = ${field.type.getDisplayString()}Model.fromMap(Map<String, dynamic>.from(map['${field.name}'] as Map));");
        }
        // List
        else if (field.type.isDartCoreList) {
          final listType = field.type as InterfaceType;
          DartType elementType = listType.typeArguments[0];
          classBuffer.writeln(
              "\t\t\tobject.${field.name} =  map['${field.name}'] != null ? map['${field.name}'].cast<${elementType.getDisplayString()}>() : [];");
        }
        // Map
        else if (field.type.isDartCoreMap) {
          final listType = field.type as InterfaceType;
          DartType valueType = listType.typeArguments[1];
          classBuffer.writeln(
              "\t\t\tobject.${field.name} = map['${field.name}'] != null ? map['${field.name}'].cast<String, ${valueType.getDisplayString()}>() : {};");
        }
        // Enum
        else if (ClassChecker.isEnumType(field.type)) {
          classBuffer.writeln(
              "\t\t\tobject.${field.name} = ${field.type.getDisplayString()}.values.firstWhere((e) => e.toString() == map['${field.name}'] as String);");
        }
        // Other types
        else {
          classBuffer.writeln(
              "\t\t\tobject.${field.name} = map['${field.name}'] as ${field.type.getDisplayString()};");
        }
      }
    }
    classBuffer.writeln("\t\t return object;");
    classBuffer.writeln("\t}");
    classBuffer.writeln();

    //End extension class
    classBuffer.writeln("}");
    classBuffer.writeln();
  }

  /// Finds all fields of a class (including those from its superclasses).
  static List<FieldElement> _findAllFieldsForClass(ClassElement classElement) {
    List<FieldElement> fields = [];

    void collect(InterfaceElement c) {
      fields.addAll(
        c.fields.where((f) => !f.isStatic && f.name != "hashCode" && f.name != "runtimeType"),
      );
      for (final st in c.allSupertypes) {
        collect(st.element);
      }
    }

    collect(classElement);

    // Deduplicate by name (child overrides parent)
    final byName = <String, FieldElement>{};
    for (final f in fields.reversed) {
      byName[f.name] = f;
    }

    return byName.values.toList();
  }

}