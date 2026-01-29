// import 'dart:async';
//
// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/firestorm.dart';
// import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
// import 'package:flutter_test_app/evaluation/data/motorcycle.dart';
//
//
// class UpdateEvaluationAPI extends EvaluationRuntime {
//
//   @override
//   run({List<String>? args}) async {
//
//     print("~~~~~~~~ FIRESTORE API UPDATE EVAL ~~~~~~~~");
//
//     FirebaseFirestore db = FirebaseFirestore.instance;
//     db.useFirestoreEmulator("127.0.0.1", 8080);
//
//     List<String> ids = [];
//     List<Motorcycle> motorcycles = [];
//
//     //Create 100 randomly generated items:
//     for (int i = 0; i < 101; i++) { //exclude first call as warmup call
//       Motorcycle motorcycle = Motorcycle(
//           Firestorm.randomID(),
//           "Suzuki",
//           "Bandit",
//           "Cruiser"
//       );
//       ids.add(motorcycle.id);
//       motorcycles.add(motorcycle);
//       DocumentReference ref = db.collection("Motorcycle").doc(motorcycle.id);
//       await ref.set(motorcycle.toMap());
//       motorcycle.brand = "Ducati";
//       motorcycle.model = "Monster";
//     }
//
//     List<int> times = [];
//     int sum = 0;
//
//     for (int i = 0; i < 101; i++) { //exclude first call as warmup call
//       Stopwatch stopwatch = Stopwatch();
//       stopwatch.start();
//
//       DocumentReference ref = db.collection("Motorcycle").doc(motorcycles[i].id);
//       await ref.update(motorcycles[i].toMap());
//
//       stopwatch.stop();
//       times.add(stopwatch.elapsedMicroseconds);
//       sum += stopwatch.elapsedMicroseconds;
//       Future.delayed(Duration(milliseconds: 100));
//     }
//
//     print("Times API: $times");
//
//   }
//
// }