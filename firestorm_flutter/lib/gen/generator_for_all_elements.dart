import 'dart:async';
import 'package:source_gen/source_gen.dart';

class GeneratorForAllElements extends Generator {

  @override
  FutureOr<String> generate(LibraryReader library, _) {
    print('Generating all elements');
    final elements = library.allElements;
    final elementsInfo = elements.map((e) {
      final elementName = e.name;
      final elementType = e.kind.toString();
      return '//Element: $elementName, Type: $elementType';
    }).join('\n  ');
    return "";
  }

}