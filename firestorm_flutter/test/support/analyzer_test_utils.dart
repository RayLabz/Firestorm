import 'dart:io';

import 'package:analyzer/dart/analysis/analysis_context_collection.dart';
import 'package:analyzer/dart/analysis/results.dart';
import 'package:analyzer/dart/element/element.dart';
import 'package:path/path.dart' as p;

Future<LibraryElement> resolveLibraryFromRelativePath(String relativePath) async {
  final filePath = p.normalize(p.absolute(relativePath));
  final sdkPath = p.normalize(p.dirname(p.dirname(Platform.resolvedExecutable)));

  final collection = AnalysisContextCollection(
    includedPaths: <String>[filePath],
    sdkPath: sdkPath,
  );
  final context = collection.contextFor(filePath);
  final result = await context.currentSession.getResolvedLibrary(filePath);

  if (result is! ResolvedLibraryResult) {
    throw StateError('Unable to resolve library at $relativePath');
  }

  return result.element;
}

Future<Map<String, ClassElement>> resolveClassMapFromLibraryPath(String relativePath) async {
  final classes = <String, ClassElement>{};
  final library = await resolveLibraryFromRelativePath(relativePath);

  for (final element in library.topLevelElements) {
    if (element is ClassElement) {
      classes[element.name] = element;
    }
  }

  return classes;
}

Future<EnumElement> resolveEnumFromLibraryPath(String relativePath, String enumName) async {
  final library = await resolveLibraryFromRelativePath(relativePath);
  return library.topLevelElements
      .whereType<EnumElement>()
      .firstWhere((element) => element.name == enumName);
}





