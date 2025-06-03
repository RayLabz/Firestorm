import 'package:analyzer/dart/element/element.dart';

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

    //Class fields:
    for (final field in aClass.fields) {
      classBuffer.writeln("\t\t\t '${field.name}': this.${field.name},");
    }

    //Find fields of parent classes and process:
    for (final parent in aClass.allSupertypes) {
      for (final parentField in parent.element.fields) {
        if (parentField.name != "hashCode" && parentField.name != "runtimeType") {
          classBuffer.writeln(
              "\t\t\t '${parentField.name}': ${parentField.name},");
        }
      }
    }

    classBuffer.writeln("\t\t };");
    classBuffer.writeln("\t }");

    //Generate fromMap() method
    classBuffer.writeln("\tstatic ${aClass.name} fromMap(Map<String, dynamic> map) {");
    classBuffer.writeln("\t\t return ${aClass.name}(");

    //Class fields:
    for (final field in aClass.fields) {
      classBuffer.writeln("\t\t\t map['${field.name}'],"); //TODO - Type conversions, e.g. toDouble, toInt etc.
    }

    //Find fields of parent classes and process:
    for (final parent in aClass.allSupertypes) {
      for (final parentField in parent.element.fields) {
        if (parentField.name != "hashCode" && parentField.name != "runtimeType") {
          classBuffer.writeln("\t\t\t map['${parentField
              .name}'],"); //TODO - Type conversions, e.g. toDouble, toInt etc.
        }
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