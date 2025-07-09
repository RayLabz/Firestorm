import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/exceptions/no_document_exception.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_test_app/computing_student.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';
import 'package:integration_test/integration_test.dart';

void main() {

  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  //Emulator settings
  const String emulatorHost = "127.0.0.1";
  const int emulatorPort = 8080;

  //Test data
  final ComputingStudent student = ComputingStudent.generateRandomStudent();
  final List<ComputingStudent> students = ComputingStudent.generateStudents(10);

  //Sets up testing for all functions
  setUpAll(() async {
    WidgetsFlutterBinding.ensureInitialized();
    await FS.init();
    FS.useEmulator(emulatorHost, emulatorPort);
    registerClasses();
  },);

  /* ----------------------------------------------------------------------*/

  /* ----- TEST CREATE ----- */

  testWidgets("Test create.one()", (tester) async {
    await FS.create.one(student);
    ComputingStudent? result = await FS.get.one<ComputingStudent>(student.id);
    assert(result != null);
  });

  testWidgets("Test create.many()", (tester) async {
    await FS.create.many(students);
    List<ComputingStudent> result = await FS.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.length == students.length);
  });

  /* ----- TEST GET ----- */
  testWidgets("Test get.one() with valid ID", (tester) async {
    ComputingStudent? result = await FS.get.one<ComputingStudent>(student.id);
    assert(result != null);
  });

  testWidgets("Test get.one() with invalid ID", (tester) async {
    ComputingStudent? result = await FS.get.one<ComputingStudent>("INVALID_ID");
    assert(result == null);
  });

  testWidgets("Test get.many()", (tester) async {
    List<ComputingStudent> result = await FS.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.length == students.length);
    for (final s in students) {
      assert(result.contains(s));
    }
  });

  /* ----- TEST UPDATE ----- */
  testWidgets("Test update.one()", (tester) async {
    student.firstname = "new name";
    await FS.update.one(student);
    ComputingStudent? result = await FS.get.one<ComputingStudent>(student.id);
    assert(result != null);
    if (result != null) {
      assert(result.firstname == "new name");
    }
  });

  testWidgets("Test update.many()", (tester) async {
    students[0].firstname = "n1";
    students[1].firstname = "n2";
    await FS.update.many(students);
    ComputingStudent? s1 = await FS.get.one<ComputingStudent>(students[0].id);
    ComputingStudent? s2 = await FS.get.one<ComputingStudent>(students[1].id);
    assert(s1!.firstname == students[0].firstname);
    assert(s2!.firstname == students[1].firstname);
  });

  /* ----- TEST DELETE ----- */
  testWidgets("Test delete.one()", (tester) async {
    await FS.create.one(student);
    await FS.delete.one(student);
    ComputingStudent? result = await FS.get.one<ComputingStudent>(student.id);
    assert(result == null);
  },);

  testWidgets("Test delete.many()", (tester) async {
    await FS.create.many(students);
    await FS.delete.many(students);
    List<ComputingStudent> result = await FS.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.isEmpty);
  });

  testWidgets("Test delete.oneWithID()", (tester) async {
    await FS.create.one(student);
    await FS.delete.oneWithID(ComputingStudent, student.id);
    ComputingStudent? result = await FS.get.one<ComputingStudent>(student.id);
    assert(result == null);
  },);

  testWidgets("Test delete.manyWithID()", (tester) async {
    await FS.create.many(students);
    await FS.delete.manyWithIDs(ComputingStudent, students.map((e) => e.id).toList());
    List<ComputingStudent> result = await FS.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.isEmpty);
  });

  testWidgets("Test delete.all()", (tester) async {
    await FS.create.many(students);
    await FS.delete.all(ComputingStudent, iAmSure: true);
    List<ComputingStudent> result = await FS.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.isEmpty);
  });

  /* ----- TEST EXISTS ----- */
  testWidgets("Test exists.one()", (tester) async {
    await FS.create.one(student);
    bool result = await FS.exists.one(student);
    assert(result);
  });

  testWidgets("Test exists.oneWithID()", (tester) async {
    await FS.create.one(student);
    bool result = await FS.exists.oneWithID(ComputingStudent, student.id);
    assert(result);
  });

  /* ----- TEST LIST/FILTER ----- */
  testWidgets("Test list.allOfClass()", (tester) async {
    await FS.delete.all(ComputingStudent, iAmSure: true);
    await FS.create.many(students);
    List<ComputingStudent> result = await FS.list.allOfClass(ComputingStudent);
    assert(result.length == students.length);
  });

  testWidgets("Test list.ofClass() without limit", (tester) async {
    List<ComputingStudent> result = await FS.list.ofClass(ComputingStudent);
    assert(result.length == students.length);
  });

  testWidgets("Test list.ofClass() with limit", (tester) async {
    List<ComputingStudent> result = await FS.list.ofClass(ComputingStudent, limit: 3);
    assert(result.length <= 3);
  });

  testWidgets("Test list.filter(), test 1", (tester) async {
    var queryResult = await FS.list.filter<ComputingStudent>(ComputingStudent)
        .orderBy("height")
        .startAt([1.70])
        .limit(3)
        .fetch();
    assert(queryResult.items.length <= 3);
    queryResult.items.forEach((element) {
      assert(element.height >= 1.70);
    },);
  });

  //Note: Does not work with emulator due to index setup.
  testWidgets("Test list.filter(), test 2", (tester) async {
    var queryResult = await FS.list.filter<ComputingStudent>(ComputingStudent)
        .orderBy("height")
        .endAt([1.70])
        .limit(5)
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

  /* ----- TEST PAGINATOR ----- */

  testWidgets("Test paginate()", (tester) async {
    await FS.delete.all(ComputingStudent, iAmSure: true);
    await FS.create.many(students);
    var paginator = FS.paginate<ComputingStudent>(limit: 3);

    var currentPage = await paginator.next();
    assert(currentPage.items.length <= 3);

    while (currentPage.items.isNotEmpty) {
      currentPage = await paginator.next();
      assert(currentPage.items.length <= 3);
    }
  });

  /* ----- TEST BATCH ----- */

  testWidgets("Test batch write", (tester) async {
    await FS.delete.all(ComputingStudent, iAmSure: true);
    FS.batch.run((batch) {
      for (var student in students) {
        batch.create.one(student);
      }
    },);
    List<ComputingStudent> result = await FS.get.many<ComputingStudent>(students.map((e) => e.id).toList());
    assert(result.length == students.length);
  });

  /* ----- TEST TRANSACTION ----- */

  testWidgets("Test transaction", (tester) async {
    await FS.delete.all(ComputingStudent, iAmSure: true);
    await FS.create.many(students);

    await FS.transaction.run((transaction) {
      for (var student in students) {
        transaction.update.one(student..firstname = "updated");
      }
      return Future.value();
    },);

    List<ComputingStudent> result = await FS.list.allOfClass(ComputingStudent);
    assert(result.length == students.length);
    for (var student in result) {
      assert(student.firstname == "updated");
    }
  });

  testWidgets("Transaction stress test", (tester) async {
    await FS.delete.all(ComputingStudent, iAmSure: true);
    await FS.delete.one(student);
    student.height = 0;
    await FS.create.one(student);

    FS.transaction.run((transaction) async {
      ComputingStudent? s = await transaction.get.one<ComputingStudent>(student.id);
      if (s != null) {
        student.height += 1;
        transaction.update.one(student);
      }
    }).then((value) {
      FS.get.one<ComputingStudent>(student.id).then((result) {
        assert(result != null);
        if (result != null) {
          assert(result.height == 1);
        }
      },);
    },);

  });


  /* ----------------------------------------------------------------------*/

  ///Tears down the tests by removing any items created in the database
  tearDownAll(() {
    FS.delete.one(student);
    FS.delete.many(students);
  },);

}