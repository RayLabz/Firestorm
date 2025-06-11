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

  // RDB.create.one(ComputingStudent.generateRandomStudent()).then((value) => print("Hey!"),);
  // FS.list.allOfClass<ComputingStudent>(ComputingStudent).then((value) {
  //   value.forEach((element) => print('"${element.id}",'),);
  // },);

  // List<ComputingStudent> students = [
  //   ComputingStudent.generateRandomStudent(),
  //   ComputingStudent.generateRandomStudent(),
  //   ComputingStudent.generateRandomStudent(),
  // ];
  //
  //
  // await RDB.create.many(students);
  // print("Created");
  // Future.delayed(Duration(seconds: 2), () async {
  //   await RDB.delete.manyWithIDs(ComputingStudent, students.map((e) => e.id).toList());
  //   print("Deleted");
  // });

  await FS.get.one<ComputingStudent>("17153").then((value) => print(value.address.city),);
  await RDB.get.one<ComputingStudent>("16472").then((value) => print(value.address.city),);


  runApp(Container());
}