import 'package:analyzer/dart/element/element.dart';
import 'package:build/build.dart';

/// A utility class to generate import statements for Dart files.
class ImportGenerator {
  static String generateImports(StringBuffer importsBuffer, AssetId asset) {
    importsBuffer.writeln("import '${asset.uri}';");
    return importsBuffer.toString();
  }
}