import 'package:cloud_firestore/cloud_firestore.dart' show FirebaseFirestore;
import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/fs/fs.dart';
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

  await FS.init();
  registerClasses();

  // FS.create(student).then((_) {
  //   print("Student created!");
  // },);

  // FS.get<ComputingStudent>("123").then((value) {
  //   print(value.firstname);
  //   print(value.grades['Science']);
  // },);

  // FS.createMany([student, student2]).then((_) {
  //   print("Students created!");
  // });

  // FS.exists(ComputingStudent, "123").then((exists) {
  //   print("Does student with ID '123' exist? $exists");
  // });



  runApp(Container());
}