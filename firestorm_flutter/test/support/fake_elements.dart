import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/element/nullability_suffix.dart';
import 'package:analyzer/dart/element/type.dart';

class TestLibraryElement implements LibraryElement {
  TestLibraryElement({required this.isDartCore});

  @override
  final bool isDartCore;

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestElement implements Element {
  TestElement({required this.displayName, this.library});

  @override
  final String displayName;

  @override
  final LibraryElement? library;

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestElementAnnotation implements ElementAnnotation {
  TestElementAnnotation(this.element);

  @override
  final Element? element;

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestClassElement implements ClassElement {
  TestClassElement({
    required this.name,
    required this.displayName,
    LibraryElement? library,
    this.fields = const <FieldElement>[],
    this.metadata = const <ElementAnnotation>[],
    this.constructors = const <ConstructorElement>[],
    this.allSupertypes = const <InterfaceType>[],
    this.unnamedConstructor,
    this.fieldLookup = const <String, FieldElement>{},
  }) : _library = library;

  @override
  final String name;

  @override
  final String displayName;

  final LibraryElement? _library;

  @override
  LibraryElement get library => _library ?? TestLibraryElement(isDartCore: false);

  @override
  final List<FieldElement> fields;

  @override
  final List<ElementAnnotation> metadata;

  @override
  final List<ConstructorElement> constructors;

  @override
  final List<InterfaceType> allSupertypes;

  @override
  final ConstructorElement? unnamedConstructor;

  final Map<String, FieldElement> fieldLookup;

  @override
  FieldElement? getField(String name) {
    for (final field in fields) {
      if (field.name == name) {
        return field;
      }
    }
    return fieldLookup[name];
  }

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestFieldElement implements FieldElement {
  TestFieldElement({
    required this.name,
    required this.type,
    this.metadata = const <ElementAnnotation>[],
    this.isStatic = false,
  });

  @override
  final String name;

  @override
  final DartType type;

  @override
  final List<ElementAnnotation> metadata;

  @override
  final bool isStatic;

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestConstructorElement implements ConstructorElement {
  TestConstructorElement({
    this.parameters = const <ParameterElement>[],
    this.isPublic = true,
  });

  @override
  final List<ParameterElement> parameters;

  @override
  final bool isPublic;

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestParameterElement implements ParameterElement {
  TestParameterElement({
    required this.name,
    required this.displayName,
    required this.type,
    this.isPositional = false,
    this.isNamed = false,
  });

  @override
  final String name;

  @override
  final String displayName;

  @override
  final DartType type;

  @override
  final bool isPositional;

  @override
  final bool isNamed;

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestEnumElement implements EnumElement {
  TestEnumElement({required this.name, required this.displayName, LibraryElement? library})
      : _library = library;

  @override
  final String name;

  @override
  final String displayName;

  final LibraryElement? _library;

  @override
  LibraryElement get library => _library ?? TestLibraryElement(isDartCore: false);

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestDartType implements DartType {
  TestDartType({
    required this.displayString,
    this.element,
    this.isDartCoreString = false,
    this.isDartCoreInt = false,
    this.isDartCoreDouble = false,
    this.isDartCoreBool = false,
    this.isDartCoreNull = false,
    this.isDartCoreList = false,
    this.isDartCoreMap = false,
    this.nullabilitySuffix = NullabilitySuffix.none,
  });

  final String displayString;

  @override
  final Element? element;

  @override
  final bool isDartCoreString;

  @override
  final bool isDartCoreInt;

  @override
  final bool isDartCoreDouble;

  @override
  final bool isDartCoreBool;

  @override
  final bool isDartCoreNull;

  @override
  final bool isDartCoreList;

  @override
  final bool isDartCoreMap;

  @override
  final NullabilitySuffix nullabilitySuffix;

  @override
  String getDisplayString({bool multiline = false, bool preferTypeAlias = false, bool withNullability = true}) {
    return displayString;
  }

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

class TestInterfaceType extends TestDartType implements InterfaceType {
  TestInterfaceType({
    required super.displayString,
    required this.element,
    this.typeArguments = const <DartType>[],
    super.isDartCoreString,
    super.isDartCoreInt,
    super.isDartCoreDouble,
    super.isDartCoreBool,
    super.isDartCoreNull,
    super.isDartCoreList,
    super.isDartCoreMap,
    super.nullabilitySuffix,
  });

  @override
  final InterfaceElement element;

  @override
  final List<DartType> typeArguments;

  @override
  dynamic noSuchMethod(Invocation invocation) => super.noSuchMethod(invocation);
}

TestElementAnnotation firestormObjectAnnotation() {
  return TestElementAnnotation(TestElement(displayName: 'FirestormObject'));
}

TestElementAnnotation excludeAnnotation() {
  return TestElementAnnotation(TestElement(displayName: 'Exclude'));
}




