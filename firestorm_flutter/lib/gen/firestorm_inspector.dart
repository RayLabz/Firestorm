import 'package:analyzer/dart/element/element.dart';
import 'package:build/build.dart';
import 'package:source_gen/source_gen.dart';

import '../annotation/firestorm_object.dart';

class FirestormGenerator extends GeneratorForAnnotation<FirestormObject> {

  @override
  dynamic generateForAnnotatedElement(Element element, ConstantReader annotation, BuildStep buildStep) {
    print('Found annotated element: ${element.name}');
    // ...
    // Make sure the element is a ClassElement
    if (element is! ClassElement) {
      return '// Not a class.';
    }

    final classElement = element;
    final className = classElement.displayName;

    // Gather details
    final fieldsInfo = classElement.fields
    // Filter out synthetic fields (like getters/setters)
        .where((f) => !f.isSynthetic)
        .map((f) {
      final fieldName = f.displayName;
      final fieldType = f.type.getDisplayString(withNullability: false);
      return '//Field: $fieldName, Type: $fieldType';
    })
        .join('\n  ');

    classElement.allSupertypes.forEach((supertype) {
      if (!supertype.isDartCoreObject) {
        print("Supertype: " + supertype.getDisplayString(withNullability: false));
      }
    });

    // Constructors (just to show example)
    final constructorInfo = classElement.constructors.map((c) {
      final params = c.parameters.map((p) {
        final paramType = p.type.getDisplayString(withNullability: false);
        return '${p.name}: $paramType';
      }).join(', ');
      return 'Constructor ${c.name.isEmpty ? '(default)' : c.name}($params)';
    }).join('\n  ');

    // Methods (just to show example)
    final methodInfo = classElement.methods
        .where((m) => !m.isSynthetic)
        .map((m) {
      final returnType = m.returnType.getDisplayString(withNullability: false);
      return 'Method: ${m.displayName}, returns $returnType';
    })
        .join('\n  ');

    print("Hello there!");

    // Generate a string (could also be code to define a helper class, etc.)
    return '''
// Inspecting class: $className
// Fields:
//   $fieldsInfo
//
// Constructors:
//   $constructorInfo
//
// Methods:
//   $methodInfo
''';
  }
}
