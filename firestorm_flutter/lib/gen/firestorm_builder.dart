import 'dart:async';
import 'package:analyzer/dart/element/element.dart';
import 'package:build/build.dart';
import 'package:colorful_text/colorful_text.dart';
import 'package:firestorm/gen/header_generator.dart';
import 'package:firestorm/gen/import_generator.dart';
import 'package:source_gen/source_gen.dart';
import 'package:glob/glob.dart';

import 'class_checker.dart';
import 'converter_generator.dart';
import 'extension_generator.dart';

class FirestormBuilder implements Builder {

  @override
  final buildExtensions = const {
    r'$lib$': ['generated/firestorm_models.dart'],
  };

  @override
  Future<void> build(BuildStep buildStep) async {

    List<ClassElement> classes = [];

    //Buffers:
    final fileBuffer = StringBuffer();
    final headerBuffer = StringBuffer();
    final importsBuffer = StringBuffer();
    final classBuffer = StringBuffer();
    final converterBuffer = StringBuffer();

    //Generate header
    HeaderGenerator.generateHeader(headerBuffer);

    importsBuffer.writeln("import 'package:firestorm/fs/fs.dart';");

    await for (final input in buildStep.findAssets(Glob('lib/**.dart'))) { //iterates through all .dart files
      if (!await buildStep.resolver.isLibrary(input)) {
        continue;
      }
      final libraryElement = await buildStep.resolver.libraryFor(input);
      final libraryReader = LibraryReader(libraryElement);

      //Perform filtering
      //1. @FirestormObject annotation
      //2. public no-arg constructor
      //3. ID field
      List<ClassElement> classesInThisFile = ClassChecker.filter(libraryReader.classes);
      classes.addAll(classesInThisFile);

      //Add into the import buffer, if there are any classes in this file
      if (classesInThisFile.isNotEmpty) {
        ImportGenerator.generateImports(importsBuffer, input);
      }

    }

    //Generate extensions:
    for (final aClass in classes) {
      ExtensionGenerator.generateExtension(classBuffer, aClass);
    }

    //Generate the converter functions
    RegistryGenerator.generateConverterFunctions(converterBuffer, classes);

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
