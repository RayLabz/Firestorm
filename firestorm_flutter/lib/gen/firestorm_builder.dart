import 'dart:async';
import 'package:analyzer/dart/element/element.dart';
import 'package:build/build.dart';
import 'package:colorful_text/colorful_text.dart';
import 'package:firestorm/gen/header_generator.dart';
import 'package:firestorm/gen/import_generator.dart';
import 'package:firestorm/gen/valid_class_holder.dart';
import 'package:source_gen/source_gen.dart';
import 'package:glob/glob.dart';

import 'class_checker.dart';
import 'converter_generator.dart';
import 'extension_generator.dart';

/// A builder that generates Firestorm model extensions and converter functions
class FirestormBuilder implements Builder {

  @override
  final buildExtensions = const {
    r'$lib$': ['generated/firestorm_models.dart'],
  };

  @override
  Future<void> build(BuildStep buildStep) async {

    //Buffers:
    final fileBuffer = StringBuffer();
    final headerBuffer = StringBuffer();
    final importsBuffer = StringBuffer();
    final classBuffer = StringBuffer();
    final converterBuffer = StringBuffer();

    //Generate header
    HeaderGenerator.generateHeader(headerBuffer);

    importsBuffer.writeln("import 'package:firestorm/fs/fs.dart';");
    importsBuffer.writeln("import 'package:firestorm/rdb/rdb.dart';");

    final Map<AssetId, Iterable<ClassElement>> allClasses = {};
    final Map<ClassElement, AssetId> assetIDs = {};
    final Map<EnumElement, AssetId> enumAssetIds = {};

    //Read the classes from the files and store in allClasses map:
    await for (final input in buildStep.findAssets(Glob('lib/**.dart'))) { //iterates through all .dart files
      if (!await buildStep.resolver.isLibrary(input)) {
        continue;
      }
      final libraryElement = await buildStep.resolver.libraryFor(input);
      final libraryReader = LibraryReader(libraryElement);
      allClasses[input] = libraryReader.classes;

      for (final aClass in libraryReader.classes) {
        assetIDs[aClass] = input; //Map each class to its AssetId
      }

      //enums:
      for (final aClass in libraryReader.enums) {
        enumAssetIds[aClass] = input; //Map each enum to its AssetId
      }

    }

    //Perform filtering
    //1. @FirestormObject annotation
    //2. public no-arg constructor
    //3. ID field
    //4. Firestore or Realtime Database support (type checks)

    ValidClassHolder allFilesClassHolder = ValidClassHolder.empty();
    for (final pair in allClasses.entries) {
      final Iterable<ClassElement> allClasses = pair.value;
      var fileClassHolder = ClassChecker.filter(allClasses);
      allFilesClassHolder.join(fileClassHolder);
    }

    //For every valid class, generate necessary imports and its extension:
    for (final validClass in allFilesClassHolder.getAllValidClasses()) {

      //Add into the import buffer, if there are any classes in this file
      ImportGenerator.generateImports(importsBuffer, assetIDs[validClass]!);

      //Generate extensions:
      ExtensionGenerator.generateExtension(
          classBuffer,
          validClass,
          allFilesClassHolder.hasFSSupport(validClass),
          allFilesClassHolder.hasRDBSupport(validClass)
      );
    }

    //Generate imports for enums:
    for (final assetID in enumAssetIds.values) {
      ImportGenerator.generateImports(importsBuffer, assetID);
    }

    //Generate the converter functions
    RegistryGenerator.generateConverterFunctions(converterBuffer, allFilesClassHolder);

    //Add everything into the file buffer
    fileBuffer.writeln(headerBuffer.toString());
    fileBuffer.writeln(importsBuffer.toString());
    fileBuffer.writeln(classBuffer.toString());
    fileBuffer.writeln(converterBuffer.toString());

    final formattedOutput = fileBuffer.toString();

    final outputId = AssetId(buildStep.inputId.package, 'lib/generated/firestorm_models.dart');
    await buildStep.writeAsString(outputId, formattedOutput);
  }
}
