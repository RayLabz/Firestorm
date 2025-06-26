import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/firestorm.dart';
import 'package:flutter_test_app/evaluation/evaluation.dart';
import 'package:flutter_test_app/evaluation/motorcycle.dart';


class CreateManyEvaluationAPI extends EvaluationRuntime {

  @override
  run({List<String>? args}) async {

    print("~~~~~~~~ FIRESTORE API CREATE MANY EVAL ~~~~~~~~");

    FirebaseFirestore db = FirebaseFirestore.instance;
    db.useFirestoreEmulator("127.0.0.1", 8080);

    List<int> times = [];
    List<Motorcycle> motorcycles = [];
    int sum = 0;

    for (int i = 0; i < 100; i++) { //exclude first call as warmup call
      Motorcycle motorcycle = Motorcycle(
          Firestorm.randomID(),
          "Suzuki",
          "Bandit",
          "Cruiser"
      );
      motorcycles.add(motorcycle);
    }

    Stopwatch stopwatch = Stopwatch();
    stopwatch.start();

    var batch = db.batch();
    for (int i = 0; i < motorcycles.length; i++) {
      DocumentReference ref = db.collection("Motorcycle").doc(motorcycles[i].id);
      batch.set(ref, motorcycles[i].toMap());
    }
    await batch.commit();

    stopwatch.stop();
    times.add(stopwatch.elapsedMicroseconds);
    sum += stopwatch.elapsedMicroseconds;
    Future.delayed(Duration(milliseconds: 100));

    print("Times API: $times");

  }

}