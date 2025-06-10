import 'package:firestorm/fs/fs.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';

import 'address.dart';
import 'computing_student.dart';

main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await FS.init();
  registerClasses();

  FS.create.one(ComputingStudent.generateRandomStudent()).then((value) => print("nice!"),);

  runApp(Container());
}