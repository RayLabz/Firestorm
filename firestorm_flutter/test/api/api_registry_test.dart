// import 'package:flutter_test/flutter_test.dart';
// import 'package:firestorm/firestorm.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:firestorm/ls/ls.dart';
// import 'package:firestorm/rdb/rdb.dart';
//
// class _Demo {
//   _Demo(this.id, this.value);
//
//   final String id;
//   final int value;
// }
//
// void main() {
//   setUp(() {
//     FS.serializers.clear();
//     FS.deserializers.clear();
//     FS.classNames.clear();
//
//     RDB.serializers.clear();
//     RDB.deserializers.clear();
//     RDB.classNames.clear();
//
//     LS.serializers.clear();
//     LS.deserializers.clear();
//     LS.classNames.clear();
//   });
//
//   test('Firestorm.randomID returns non-empty unique IDs', () {
//     final one = Firestorm.randomID();
//     final two = Firestorm.randomID();
//
//     expect(one, isNotEmpty);
//     expect(two, isNotEmpty);
//     expect(one, isNot(equals(two)));
//   });
//
//   test('FS registry APIs register serializers, deserializers and class names', () {
//     FS.registerSerializer<_Demo>((obj) => <String, dynamic>{'id': obj.id, 'value': obj.value});
//     FS.registerDeserializer<_Demo>((map) => _Demo(map['id'] as String, map['value'] as int));
//     FS.registerClassName(_Demo, 'Demo');
//
//     final serializer = FS.serializers[_Demo]!;
//     final deserializer = FS.deserializers[_Demo]!;
//
//     expect(serializer(_Demo('x', 1)), <String, dynamic>{'id': 'x', 'value': 1});
//     expect((deserializer(<String, dynamic>{'id': 'y', 'value': 2}) as _Demo).id, 'y');
//     expect(FS.classNames[_Demo], 'Demo');
//   });
//
//   test('RDB registry APIs and path constructors behave as expected', () {
//     RDB.registerSerializer<_Demo>((obj) => <String, dynamic>{'id': obj.id, 'value': obj.value});
//     RDB.registerDeserializer<_Demo>((map) => _Demo(map['id'] as String, map['value'] as int));
//     RDB.registerClassName(_Demo, 'Demo');
//
//     final serializer = RDB.serializers[_Demo]!;
//
//     expect(serializer(_Demo('x', 1)), <String, dynamic>{'id': 'x', 'value': 1});
//     expect(RDB.classNames[_Demo], 'Demo');
//     expect(RDB.constructPathForClass('Demo'), 'Demo');
//     expect(RDB.constructPathForClass('Demo', subcollection: 'archived'), 'Demo:archived');
//     expect(RDB.constructPathForClassAndID('Demo', 'abc'), 'Demo/abc');
//     expect(
//       RDB.constructPathForClassAndID('Demo', 'abc', subcollection: 'archived'),
//       'Demo:archived/abc',
//     );
//   });
//
//   test('LS registry APIs register serializers, deserializers and class names', () {
//     LS.registerSerializer<_Demo>((obj) => <String, dynamic>{'id': obj.id, 'value': obj.value});
//     LS.registerDeserializer<_Demo>((map) => _Demo(map['id'] as String, map['value'] as int));
//     LS.registerClassName(_Demo, 'Demo');
//
//     final serializer = LS.serializers[_Demo]!;
//     final deserializer = LS.deserializers[_Demo]!;
//
//     expect(serializer(_Demo('x', 1)), <String, dynamic>{'id': 'x', 'value': 1});
//     expect((deserializer(<String, dynamic>{'id': 'z', 'value': 3}) as _Demo).id, 'z');
//     expect(LS.classNames[_Demo], 'Demo');
//   });
// }
//
