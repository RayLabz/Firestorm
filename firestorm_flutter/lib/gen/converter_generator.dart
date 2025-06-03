import 'package:analyzer/dart/element/element.dart';

class RegistryGenerator {

  static String generateConverterFunctions(StringBuffer converterBuffer, final List<ClassElement> classes) {

    //Registry (map):
    converterBuffer.writeln("// - - - - - - - Registry - - - - - - -");
    converterBuffer.writeln("final Map<Type, Map<String, dynamic> Function(dynamic)> toMapRegistry = {");
    for (final aClass in classes) {
      converterBuffer.writeln("\t${aClass.displayName}: (object) => (object as ${aClass.displayName}).toMap(),");
    }
    converterBuffer.writeln("};");
    converterBuffer.writeln();

    //Registry converter function:
    converterBuffer.writeln("Map<String, dynamic> convertToMap(dynamic object) {");
    converterBuffer.writeln("\tfinal handler = toMapRegistry[object.runtimeType];");
    converterBuffer.writeln("\tif (handler != null) {");
    converterBuffer.writeln("\t\treturn handler(object);");
    converterBuffer.writeln("\t}");
    converterBuffer.writeln("\tthrow UnsupportedError('toMap not implemented for type: \${object.runtimeType}');");
    converterBuffer.writeln("}");
    converterBuffer.writeln();

    //Generate serializer registrations for each class:
    converterBuffer.writeln("registerClasses() {");
    for (final aClass in classes) {
      converterBuffer.writeln("\tFS.register<${aClass.displayName}>((object) => object.toMap());");
    }
    converterBuffer.writeln("}");
    converterBuffer.writeln();

    return converterBuffer.toString();
  }

}