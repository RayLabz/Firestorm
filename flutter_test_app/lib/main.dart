import 'package:cloud_firestore/cloud_firestore.dart' show FirebaseFirestore;
import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_test_app/computing_student.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';

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
    "abcd1234"
  );

  registerClasses();
  await FS.init();
  FS.create(student).then((_) {
    print("Student created!");
  },);


  runApp(Container());
}