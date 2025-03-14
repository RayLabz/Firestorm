import 'dart:async';
import 'package:build/build.dart';
import 'package:source_gen/source_gen.dart';
import 'package:glob/glob.dart';

class FirestormBuilder implements Builder {

  @override
  final buildExtensions = const {
    r'$lib$': ['generated/firestorm_models.dart'],
  };

  @override
  Future<void> build(BuildStep buildStep) async {
    final buffer = StringBuffer();
    buffer.writeln('// GENERATED CODE - DO NOT MODIFY BY HAND\n');
    buffer.writeln('class CombinedRegistry {');

    await for (final input in buildStep.findAssets(Glob('lib/**.dart'))) {
      if (!await buildStep.resolver.isLibrary(input)) continue;
      final libraryElement = await buildStep.resolver.libraryFor(input);
      final libraryReader = LibraryReader(libraryElement);

      for (final clazz in libraryReader.classes) {
        if (clazz.metadata.any((m) => m.element?.displayName == 'FirestormObject')) {
          buffer.writeln('  static const String ${clazz.name} = \'${clazz.name}\';');
        }
      }
    }

    buffer.writeln('}');

    // Optional formatting:
    // final formatter = DartFormatter();
    // final formattedOutput = DartFormatter().format(buffer.toString());

    final formattedOutput = buffer.toString();

    final outputId = AssetId(buildStep.inputId.package, 'lib/generated/firestorm_models.dart');
    await buildStep.writeAsString(outputId, formattedOutput);
  }
}
