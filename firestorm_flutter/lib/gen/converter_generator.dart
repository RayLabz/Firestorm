import 'package:firestorm/gen/valid_class_holder.dart';

/// Generates converter functions for converting objects to and from maps.
class RegistryGenerator {

  static String generateConverterFunctions(StringBuffer converterBuffer, final ValidClassHolder holder) {

    //Registry (map):
    converterBuffer.writeln("// - - - - - - - Registry - - - - - - -");
    converterBuffer.writeln("final Map<Type, Map<String, dynamic> Function(dynamic)> toMapRegistry = {");
    for (final aClass in holder.getAllValidClasses()) {
      converterBuffer.writeln("\t${aClass.displayName}: (object) => (object as ${aClass.displayName}).toMap(),");
    }
    converterBuffer.writeln("};");
    converterBuffer.writeln();

    converterBuffer.writeln("final Map<Type, dynamic Function(Map<String, dynamic>)> fromMapRegistry = {");
    for (final aClass in holder.getAllValidClasses()) {
      converterBuffer.writeln("\t${aClass.displayName}: (map) => ${aClass.displayName}Model.fromMap(map),");
    }
    converterBuffer.writeln("};");
    converterBuffer.writeln();

    //Registry converter function, serializer:
    converterBuffer.writeln("Map<String, dynamic> convertToMap(dynamic object) {");
    converterBuffer.writeln("\tfinal serializer = toMapRegistry[object.runtimeType];");
    converterBuffer.writeln("\tif (serializer != null) {");
    converterBuffer.writeln("\t\treturn serializer(object);");
    converterBuffer.writeln("\t}");
    converterBuffer.writeln("\tthrow UnsupportedError('toMap() not implemented for type: \${object.runtimeType}');");
    converterBuffer.writeln("}");
    converterBuffer.writeln();

    //Registry converter function, deserializer:
    converterBuffer.writeln("T convertFromMap<T>(Map<String, dynamic> map) {");
    converterBuffer.writeln("\tfinal deserializer = fromMapRegistry[T];");
    converterBuffer.writeln("\tif (deserializer == null) {");
    converterBuffer.writeln("\t\tthrow UnsupportedError('fromMap() not implemented for type: \${T.toString()}');");
    converterBuffer.writeln("\t}");
    converterBuffer.writeln("\treturn deserializer(map) as T;");
    converterBuffer.writeln("}");
    converterBuffer.writeln();

    //Generate serializer registrations for each class:
    converterBuffer.writeln("registerClasses() {");
    //Firestore:
    for (final aClass in holder.fsValidClasses) {
      converterBuffer.writeln("\tFS.registerSerializer<${aClass.displayName}>((object) => object.toMap());");
      converterBuffer.writeln("\tFS.registerDeserializer<${aClass.displayName}>((map) => ${aClass.displayName}Model.fromMap(map));");
    }
    //Realtime Database:
    for (final aClass in holder.rdbValidClasses) {
      converterBuffer.writeln("\tRDB.registerSerializer<${aClass.displayName}>((object) => object.toMap());");
      converterBuffer.writeln("\tRDB.registerDeserializer<${aClass.displayName}>((map) => ${aClass.displayName}Model.fromMap(map));");
    }
    converterBuffer.writeln("}");
    converterBuffer.writeln();

    return converterBuffer.toString();
  }

}