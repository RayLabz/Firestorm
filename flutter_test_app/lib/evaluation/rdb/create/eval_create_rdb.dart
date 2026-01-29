// import 'dart:async';
//
// import 'package:firebase_database/firebase_database.dart';
// import 'package:firestorm/firestorm.dart';
// import 'package:firestorm/rdb/rdb.dart';
// import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
// import 'package:flutter_test_app/evaluation/data/motorcycle.dart';
//
//
// class CreateEvaluationFRDB extends EvaluationRuntime {
//
//   @override
//   run({List<String>? args}) async {
//
//     print("~~~~~~~~ Firestorm CREATE EVAL ~~~~~~~~");
//
//     FirebaseDatabase db = FirebaseDatabase.instance;
//     db.useDatabaseEmulator("127.0.0.1", 9000);
//
//     List<int> times = [];
//     int sum = 0;
//
//     for (int i = 0; i < 101; i++) { //exclude first call as warmup call
//       Motorcycle motorcycle = Motorcycle(
//           Firestorm.randomID(),
//           "Suzuki",
//           "Bandit",
//           "Cruiser"
//       );
//       Stopwatch stopwatch = Stopwatch();
//       stopwatch.start();
//
//       await RDB.create.one(motorcycle);
//
//       stopwatch.stop();
//       times.add(stopwatch.elapsedMicroseconds);
//       sum += stopwatch.elapsedMicroseconds;
//       Future.delayed(Duration(milliseconds: 100));
//     }
//
//     print("Times Firestorm: $times");
//
//   }
//
// }