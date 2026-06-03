import 'package:flutter_test/flutter_test.dart';
import 'package:firestorm/gen/class_checker.dart';

import '../support/fake_elements.dart';

void main() {
  late TestClassElement validEntity;
  late TestClassElement missingIdEntity;
  late TestClassElement unsupportedEntity;
  late TestClassElement enumEntity;
  late TestClassElement baseEntity;

  setUp(() {
    final dartCoreLibrary = TestLibraryElement(isDartCore: true);
    final customLibrary = TestLibraryElement(isDartCore: false);

    final stringType = TestDartType(
      displayString: 'String',
      isDartCoreString: true,
      element: TestElement(displayName: 'String', library: dartCoreLibrary),
    );
    final intType = TestDartType(
      displayString: 'int',
      isDartCoreInt: true,
      element: TestElement(displayName: 'int', library: dartCoreLibrary),
    );

    final durationType = TestDartType(
      displayString: 'Duration',
      element: TestElement(displayName: 'Duration', library: customLibrary),
    );

    final roleEnum = TestEnumElement(
      name: 'Role',
      displayName: 'Role',
      library: customLibrary,
    );
    final enumType = TestInterfaceType(
      displayString: 'Role',
      element: roleEnum,
    );

    baseEntity = TestClassElement(
      name: 'BaseEntity',
      displayName: 'BaseEntity',
      fields: <TestFieldElement>[
        TestFieldElement(name: 'id', type: stringType),
      ],
    );

    validEntity = TestClassElement(
      name: 'ValidEntity',
      displayName: 'ValidEntity',
      metadata: <TestElementAnnotation>[firestormObjectAnnotation()],
      fields: <TestFieldElement>[
        TestFieldElement(name: 'count', type: intType),
      ],
      allSupertypes: <TestInterfaceType>[
        TestInterfaceType(displayString: 'BaseEntity', element: baseEntity),
      ],
    );

    missingIdEntity = TestClassElement(
      name: 'MissingIdEntity',
      displayName: 'MissingIdEntity',
      metadata: <TestElementAnnotation>[firestormObjectAnnotation()],
      fields: <TestFieldElement>[TestFieldElement(name: 'count', type: intType)],
    );

    unsupportedEntity = TestClassElement(
      name: 'UnsupportedEntity',
      displayName: 'UnsupportedEntity',
      metadata: <TestElementAnnotation>[firestormObjectAnnotation()],
      fields: <TestFieldElement>[
        TestFieldElement(name: 'id', type: stringType),
        TestFieldElement(name: 'ttl', type: durationType),
      ],
    );

    enumEntity = TestClassElement(
      name: 'EnumEntity',
      displayName: 'EnumEntity',
      metadata: <TestElementAnnotation>[firestormObjectAnnotation()],
      fields: <TestFieldElement>[
        TestFieldElement(name: 'id', type: stringType),
        TestFieldElement(name: 'role', type: enumType),
      ],
    );
  });

  test('findAnnotatedClasses only returns @FirestormObject classes', () {
    final annotated = ClassChecker.findAnnotatedClasses(
      <TestClassElement>[validEntity, missingIdEntity, unsupportedEntity, enumEntity, baseEntity],
    );
    final names = annotated.map((e) => e.name).toSet();

    expect(names, containsAll(<String>{
      'ValidEntity',
      'MissingIdEntity',
      'UnsupportedEntity',
      'EnumEntity',
    }));
    expect(names, isNot(contains('BaseEntity')));
  });

  test('findClassesWithIDField accepts inherited id field', () {
    final candidates = <TestClassElement>[validEntity, missingIdEntity, enumEntity];

    final withId = ClassChecker.findClassesWithIDField(candidates)
        .map((e) => e.name)
        .toSet();

    expect(withId, containsAll(<String>{'ValidEntity', 'EnumEntity'}));
    expect(withId, isNot(contains('MissingIdEntity')));
  });

  test('filter keeps only classes with supported FS and RDB field types', () {
    final filtered = ClassChecker.filter(
      <TestClassElement>[validEntity, missingIdEntity, unsupportedEntity, enumEntity, baseEntity],
    );
    final allNames = filtered.getAllValidClasses().map((e) => e.name).toSet();

    expect(allNames, containsAll(<String>{'ValidEntity', 'EnumEntity'}));
    expect(allNames, isNot(contains('MissingIdEntity')));
    expect(allNames, isNot(contains('UnsupportedEntity')));
  });

  test('isEnumType detects enum field types', () {
    final roleField = enumEntity.getField('role')!;

    expect(ClassChecker.isEnumType(roleField.type), isTrue);
  });
}





