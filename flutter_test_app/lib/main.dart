import 'package:firestorm/fs/fs.dart';
import 'package:firestorm/rdb/rdb.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';

import 'address.dart';
import 'computing_student.dart';

main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await FS.init();
  await RDB.init();
  registerClasses();

  List<ComputingStudent> students = ComputingStudent.generateStudents(3);
  await RDB.create.many(students);
  print("Created.");

  students[0].firstname = "Nicos";
  students[1].firstname = "Kasenides";
  students[2].firstname = "Panayiota";

  await RDB.update.many(students);
  print("Updated");

  runApp(Container());
}