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
    ComputingStudent? result = await RDB.get.one<ComputingStudent>(student.id);
    assert(result != null);
  });

  testWidgets("Test create.many()", (tester) async {
    await RDB.create.many(students);
    List<ComputingStudent> result = await RDB.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.length == students.length);
  });

  /* ----- TEST GET ----- */
  testWidgets("Test get.one()", (tester) async {
    ComputingStudent? result = await RDB.get.one<ComputingStudent>(student.id);
    assert(result != null);
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
    ComputingStudent? result = await RDB.get.one<ComputingStudent>(student.id);
    assert(result != null);
    if (result != null) {
      assert(result.firstname == "new name");
    }
  });

  testWidgets("Test update.many()", (tester) async {
    students[0].firstname = "n1";
    students[1].firstname = "n2";
    await RDB.update.many(students);
    ComputingStudent? s1 = await RDB.get.one<ComputingStudent>(students[0].id);
    ComputingStudent? s2 = await RDB.get.one<ComputingStudent>(students[1].id);
    assert(s1!.firstname == students[0].firstname);
    assert(s2!.firstname == students[1].firstname);
  });

  /* ----- TEST DELETE ----- */
  testWidgets("Test delete.one()", (tester) async {
    await RDB.create.one(student);
    await RDB.delete.one(student);
    ComputingStudent? result = await RDB.get.one<ComputingStudent>(student.id);
    assert(result == null);
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
    ComputingStudent? result = await RDB.get.one<ComputingStudent>(student.id);
    assert(result == null);
  },);

  testWidgets("Test delete.manyWithID()", (tester) async {
    await RDB.create.many(students);
    await RDB.delete.manyWithIDs(ComputingStudent, students.map((e) => e.id).toList());
    List<ComputingStudent> result = await RDB.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.isEmpty);
  });

  testWidgets("Test delete.all()", (tester) async {
    await RDB.create.many(students);
    await RDB.delete.all(ComputingStudent, iAmSure: true);
    List<ComputingStudent> result = await RDB.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.isEmpty);
  });

  /* ----- TEST EXISTS ----- */
  testWidgets("Test exists.one()", (tester) async {
    await RDB.create.one(student);
    bool result = await RDB.exists.one(student);
    assert(result);
  });

  testWidgets("Test exists.oneWithID()", (tester) async {
    await RDB.create.one(student);
    bool result = await RDB.exists.oneWithID(ComputingStudent, student.id);
    assert(result);
  });

  /* ----- TEST LIST/FILTER ----- */
  testWidgets("Test list.allOfClass()", (tester) async {
    await RDB.delete.all(ComputingStudent, iAmSure: true);
    await RDB.create.many(students);
    List<ComputingStudent> result = await RDB.list.allOfClass(ComputingStudent);
    assert(result.length == students.length);
  });

  testWidgets("Test list.ofClass() without limit", (tester) async {
    List<ComputingStudent> result = await RDB.list.ofClass(ComputingStudent);
    assert(result.length == students.length);
  });

  testWidgets("Test list.ofClass() with limit", (tester) async {
    List<ComputingStudent> result = await RDB.list.ofClass(ComputingStudent, limit: 3);
    assert(result.length <= 3);
  });

  testWidgets("Test list.filter(), test 1", (tester) async {
    var queryResult = await RDB.list.filter<ComputingStudent>(ComputingStudent)
        .limitToFirst(3)
        .startAt(1.70)
        .fetch();
    assert(queryResult.items.length <= 3);
    queryResult.items.forEach((element) {
      assert(element.height >= 1.70);
    },);
  });

  //Note: Does not work with emulator due to index setup.
  testWidgets("Test list.filter(), test 2", (tester) async {
    var queryResult = await RDB.list.filter<ComputingStudent>(ComputingStudent)
        .limitToLast(5)
        .endAt(1.70)
        .orderByChild("height")
        .fetch();
    assert(queryResult.items.length <= 5);
    queryResult.items.forEach((element) {
      assert(element.height <= 1.70);
    },);

    //check sort:
    for (int i = 0; i < queryResult.items.length - 1; i++) {
      if (i + 1 < queryResult.items.length) {
        assert(queryResult.items[i].height <= queryResult.items[i + 1].height);
      }
    }
  });

  /* ----------------------------------------------------------------------*/

  ///Tears down the tests by removing any items created in the database
  tearDownAll(() {
    RDB.delete.one(student);
    RDB.delete.many(students);
  },);

}