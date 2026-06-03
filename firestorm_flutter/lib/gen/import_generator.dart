import 'package:build/build.dart';

/// A utility class to generate import statements for Dart files.
class ImportGenerator {
  static String generateImports(StringBuffer importsBuffer, AssetId asset) {
    importsBuffer.writeln("import '${asset.uri}';");
    return importsBuffer.toString();
  }

  static String generateImportFromString(StringBuffer importsBuffer, String importPath) {
    importsBuffer.writeln("import '$importPath';");
    return importsBuffer.toString();
  }

  static String generateManualImport(StringBuffer importsBuffer, String importPath) {
    importsBuffer.writeln(importPath);
    return importsBuffer.toString();
  }

}