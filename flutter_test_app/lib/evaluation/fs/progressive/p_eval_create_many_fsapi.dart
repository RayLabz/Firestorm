// import 'dart:async';
//
// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/firestorm.dart';
// import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
// import 'package:flutter_test_app/evaluation/data/motorcycle.dart';
//
//
// class ProgressiveCreateManyEvaluationAPI extends EvaluationRuntime {
//
//   @override
//   run({List<String>? args}) async {
//
//     print("~~~~~~~~ FIRESTORE P - API CREATE MANY EVAL ~~~~~~~~");
//
//     FirebaseFirestore db = FirebaseFirestore.instance;
//     db.useFirestoreEmulator("127.0.0.1", 8080);
//
//     final int startingNumber = 0;
//     final int endingNumber = 500;
//     final int increment = 50;
//
//     Map<int, int> averageTimes = {};
//
//     for (int currentNumber = startingNumber; currentNumber <= endingNumber; currentNumber += increment) {
//       List<int> times = [];
//       List<Motorcycle> motorcycles = [];
//       int sum = 0;
//
//       for (int i = 0; i < currentNumber; i++) {
//         Motorcycle motorcycle = Motorcycle(
//             Firestorm.randomID(),
//             "Suzuki",
//             "Bandit",
//             "Cruiser"
//         );
//         motorcycles.add(motorcycle);
//       }
//
//       Stopwatch stopwatch = Stopwatch();
//       stopwatch.start();
//
//       var batch = db.batch();
//       for (int i = 0; i < motorcycles.length; i++) {
//         DocumentReference ref = db.collection("Motorcycle").doc(motorcycles[i].id);
//         batch.set(ref, motorcycles[i].toMap());
//       }
//       await batch.commit();
//
//       stopwatch.stop();
//       times.add(stopwatch.elapsedMilliseconds);
//       sum += stopwatch.elapsedMilliseconds;
//       averageTimes[currentNumber] = sum ~/ times.length;
//
//       // print("Times Firestore API for $i objects:\n\n $times");
//
//       Future.delayed(Duration(milliseconds: 100));
//     }
//
//     print("FIRESTORE API AVERAGE TIMES:");
//     for (int i = startingNumber; i <= endingNumber; i += increment) {
//       print("$i\t${averageTimes[i]}\t\t(ms)");
//     }
//
//   }
//
// }