import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/exceptions/no_document_exception.dart';
import 'package:firestorm/rdb/rdb.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_test_app/computing_student.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';
import 'package:integration_test/integration_test.dart';

void main() {

  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  //Emulator settings
  const String emulatorHost = "127.0.0.1";
  const int emulatorPort = 9000;

  //Test data
  final ComputingStudent student = ComputingStudent.generateRandomStudent();
  final List<ComputingStudent> students = ComputingStudent.generateStudents(10);

  //Sets up testing for all functions
  setUpAll(() async {
    WidgetsFlutterBinding.ensureInitialized();
    await RDB.init();
    RDB.useEmulator(emulatorHost, emulatorPort);
    registerClasses();
  },);

  /* ----------------------------------------------------------------------*/

  /* ----- TEST CREATE ----- */

  testWidgets("Test create.one()", (tester) async {
    await RDB.create.one(student);
    try {
      ComputingStudent result = await RDB.get.one<ComputingStudent>(student.id);
      assert(true);
    }
    catch(e) {
      expect(e, isA<NoDocumentException>());
      assert(false);
    }
  });

  testWidgets("Test create.many()", (tester) async {
    await RDB.create.many(students);
    List<ComputingStudent> result = await RDB.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.length == students.length);
  });

  /* ----- TEST GET ----- */
  testWidgets("Test get.one()", (tester) async {
    try {
      ComputingStudent result = await RDB.get.one<ComputingStudent>(student.id);
      assert(true);
    }
    catch(e) {
      expect(e, isA<NoDocumentException>());
      assert(false);
    }
  });

  testWidgets("Test get.many()", (tester) async {
    List<ComputingStudent> result = await RDB.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.length == students.length);
    for (final s in students) {
      assert(result.contains(s));
    }
  });

  /* ----- TEST UPDATE ----- */
  testWidgets("Test update.one()", (tester) async {
    student.firstname = "new name";
    await RDB.update.one(student);
    ComputingStudent result = await RDB.get.one<ComputingStudent>(student.id);
    assert(result.firstname == "new name");
  });

  testWidgets("Test update.many()", (tester) async {
    students[0].firstname = "n1";
    students[1].firstname = "n2";
    await RDB.update.many(students);
    ComputingStudent s1 = await RDB.get.one<ComputingStudent>(students[0].id);
    ComputingStudent s2 = await RDB.get.one<ComputingStudent>(students[1].id);
    assert(s1.firstname == students[0].firstname);
    assert(s2.firstname == students[1].firstname);
  });

  /* ----- TEST DELETE ----- */
  testWidgets("Test delete.one()", (tester) async {
    await RDB.create.one(student);
    await RDB.delete.one(student);
    try {
      ComputingStudent result = await RDB.get.one<ComputingStudent>(student.id);
      assert(false);
    }
    catch(e) {
      if (e.runtimeType == NoDocumentException) {
        assert(true);
      }
      assert(false);
    }
  },);

  testWidgets("Test delete.many()", (tester) async {
    await RDB.create.many(students);
    await RDB.delete.many(students);
    List<ComputingStudent> result = await RDB.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.isEmpty);
  });

  testWidgets("Test delete.oneWithID()", (tester) async {
    await RDB.create.one(student);
    await RDB.delete.oneWithID(ComputingStudent, student.id);
    try {
      ComputingStudent result = await RDB.get.one<ComputingStudent>(student.id);
      assert(false);
    }
    catch(e) {
      if (e.runtimeType == NoDocumentException) {
        assert(true);
      }
      assert(false);
    }
  },);

  testWidgets("Test delete.manyWithID()", (tester) async {
    await RDB.create.many(students);
    await RDB.delete.manyWithIDs(ComputingStudent, students.map((e) => e.id).toList());
    List<ComputingStudent> result = await RDB.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.isEmpty);
  });

  /* ----------------------------------------------------------------------*/

  ///Tears down the tests by removing any items created in the database
  tearDownAll(() {
    RDB.delete.one(student);
    RDB.delete.many(students);
  },);

}