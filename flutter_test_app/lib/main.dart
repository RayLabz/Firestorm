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

  // await RDB.create.many(ComputingStudent.generateStudents(3), subcollection: "topClass");

  // RDB.list.filter<ComputingStudent>(ComputingStudent, subcollection: "topClass")
  // .orderByChild("height")
  // .startAt(1.70)
  // .endAt(1.90)
  // .
  // .fetch()
  // .then((value) {
  //   value.items.forEach((element) => print(element.height),);
  // },);

  runApp(Container());
}