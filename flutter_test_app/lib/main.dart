import 'package:firestorm/fs/fs.dart';
import 'package:firestorm/fs/queries/fs_paginator.dart';
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

  // ComputingStudent computingStudent = ComputingStudent.generateRandomStudent();
  // await FS.create.one(computingStudent);
  // print("Created: ${computingStudent.id}");

  // FSPaginator<ComputingStudent> paginator = FS.paginate<ComputingStudent>();
  //
  // var fsQueryResult = await paginator.next();
  // print("Printing page 1");
  // fsQueryResult.items.forEach((element) => print(element.id),);
  //
  // fsQueryResult = await paginator.next();
  // print("Printing page 2");
  // fsQueryResult.items.forEach((element) => print(element.id),);

  // List<ComputingStudent> students = ComputingStudent.generateStudents(3);
  // await RDB.create.many(students);
  // print("Created.");
  //
  // RDB.listen.toObjects<ComputingStudent>(students, onCreate: (object) {
  //   print("Created: ${object.id}");
  // }, onChange: (object) {
  //   print("Changed: ${object.id}");
  // }, onDelete: () {
  //   print("Deleted");
  // });

  runApp(Container());
}