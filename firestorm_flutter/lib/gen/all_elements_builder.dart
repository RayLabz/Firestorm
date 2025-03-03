import 'package:source_gen/source_gen.dart';
import 'package:build/build.dart';
import 'generator_for_all_elements.dart';

/// A top-level function that creates your builder.
Builder allElementsBuilder(BuilderOptions options) =>
    LibraryBuilder(
      GeneratorForAllElements(),
      generatedExtension: '.g.dart',
    );