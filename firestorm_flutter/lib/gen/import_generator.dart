import 'package:analyzer/dart/element/element.dart';
import 'package:build/build.dart';

class ImportGenerator {
  static String generateImports(StringBuffer importsBuffer, AssetId asset) {
    importsBuffer.writeln("import '${asset.uri}';");
    return importsBuffer.toString();
  }
}