// import 'dart:async';
//
// import 'package:firebase_database/firebase_database.dart';
// import 'package:firestorm/firestorm.dart';
// import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
// import 'package:flutter_test_app/evaluation/data/motorcycle.dart';
//
//
// class CreateEvaluationRDBAPI extends EvaluationRuntime {
//
//   @override
//   run({List<String>? args}) async {
//
//     print("~~~~~~~~ RDB API CREATE EVAL ~~~~~~~~");
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
//       DatabaseReference ref = db.ref("Motorcycle/${motorcycle.id}");
//       await ref.set(motorcycle.toMap());
//
//       stopwatch.stop();
//       times.add(stopwatch.elapsedMicroseconds);
//       sum += stopwatch.elapsedMicroseconds;
//       Future.delayed(Duration(milliseconds: 100));
//     }
//
//     print("Times RDB API: $times");
//
//   }
//
// }