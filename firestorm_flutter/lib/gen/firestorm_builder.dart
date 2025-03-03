// lib/src/firestorm_builder.dart
import 'package:build/build.dart';
import 'package:firestorm/gen/firestorm_inspector.dart';
import 'package:firestorm/gen/generator_for_all_elements.dart';
import 'package:source_gen/source_gen.dart';

/// A top-level function that creates your builder.
Builder firestormBuilder(BuilderOptions options) =>
    LibraryBuilder(
      FirestormGenerator(),
      generatedExtension: '.g.dart',
    );