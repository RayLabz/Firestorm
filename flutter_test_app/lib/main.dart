import 'package:cloud_firestore/cloud_firestore.dart' show FirebaseFirestore;
import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:firestorm/fs/transactions/fs_transaction_handler.dart';
import 'package:firestorm/type/fs_types.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_test_app/computing_student.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';

import 'address.dart';

main() async {
  WidgetsFlutterBinding.ensureInitialized();
  ComputingStudent student = ComputingStudent(
    "123",
    "John",
    "Smith",
    21,
    1.77,
    true,
    ["Jane", "Doe"],
    "ABC123",
    "School of Computing",
    "Software Engineering",
    "abcd1234",
    Address("1", "Main St", "Springfield"),
    {"Math": 90, "Science": 85},
  );

  ComputingStudent student2 = ComputingStudent(
    "456",
    "Alice",
    "Johnson",
    22,
    1.65,
    false,
    ["Bob", "Charlie"],
    "XYZ456",
    "School of Arts",
    "Graphic Design",
    null,
    Address("2", "Second St", "Metropolis"),
    {"Art": 95, "History": 88},
  );

  ComputingStudent student3 = ComputingStudent(
    "789",
    "Bob",
    "Brown",
    23,
    1.80,
    true,
    ["Alice", "Charlie"],
    "LMN789",
    "School of Engineering",
    "Civil Engineering",
    null,
    Address("3", "Third St", "Gotham"),
    {"Physics": 92, "Chemistry": 89},
  );

  await FS.init();
  registerClasses();

  // FS.create.one(student3).then((_) {
  //   print("Student created!");
  // },);
  //
  // FS.get.one<ComputingStudent>("123").then((value) {
  //   print(value.firstname);
  //   print(value.grades['Science']);
  // },);
  //
  // FS.create.many([student2, student]).then((_) {
  //   print("Students created!");
  // });
  //
  // FS.exists.one(ComputingStudent, "123").then((exists) {
  //   print("Does student with ID '123' exist? $exists");
  // });
  //
  // FS.get.many<ComputingStudent>(["123", "456"]).then((students) {
  //   for (var student in students) {
  //     print("Student ID: ${student.id}, Name: ${student.firstname} ${student.lastname}");
  //   }
  // });
  //
  // student.height = 100;
  // FS.update.one(student).then((_) {
  //   print("Student updated!");
  // });
  //
  // student.height = 200;
  // student2.height = 300;
  // FS.update.many([student, student2]).then((_) {
  //   print("Students updated!");
  // });
  //
  // FS.delete.oneWithID(ComputingStudent, "789").then((_) {
  //   print("Student with ID '789' deleted!");
  // });
  //
  // FS.delete.one(student3).then((_) {
  //   print("Student deleted!");
  // });
  //
  // FS.delete.manyWithIDs(ComputingStudent, ["456", "789"]).then((_) {
  //   print("Students with IDs '456' and '789' deleted!");
  // });
  //
  // FS.delete.many([student, student2]).then((_) {
  //   print("Students deleted!");
  // });

  // FS.delete.all(ComputingStudent, iAmSure: true).then((_) {
  //   print("All ComputingStudents deleted!");
  // });

  // var documentFromObject = FS.reference.documentFromObject(student);
  // print(documentFromObject);

  // var documentFromID = FS.reference.documentFromID(ComputingStudent, "123");
  // print(documentFromID);

  // var documentFromPath = FS.reference.documentFromPath("students/123");
  // print(documentFromPath);

  // var collection = FS.reference.collection(ComputingStudent);
  // print(collection);

  // FS.transaction.run((tx) {
  //   tx.create.one(student);
  //   tx.create.one(student2);
  //   return tx.create.one(student3);
  // });

  // FS.batch.run((batch) {
  //   batch.create.one(student);
  //   batch.create.one(student2);
  //   batch.create.one(student3);
  // },);

  // FS.list.allOfClass<ComputingStudent>(ComputingStudent).then((value) {
  //   value.forEach((element) {
  //     print(element.firstname);
  //   },);
  // },);

  // FS.list.ofClass<ComputingStudent>(ComputingStudent).then((value) {
  //   value.forEach((element) {
  //     print(element.firstname);
  //   },);
  // },);

  runApp(Container());
}