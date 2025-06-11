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

  await RDB.get.many<ComputingStudent>(["16472", "27106"]).then((value) {
    value.forEach((element) {
      print("ID: ${element.id}, City: ${element.address.city}");
    });
  });

  runApp(Container());
}