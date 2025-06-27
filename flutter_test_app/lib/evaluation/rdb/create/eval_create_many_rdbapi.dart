import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/firestorm.dart';
import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
import 'package:flutter_test_app/evaluation/data/motorcycle.dart';


class CreateManyEvaluationRDBAPI extends EvaluationRuntime {

  @override
  run({List<String>? args}) async {

    print("~~~~~~~~ RDB API CREATE MANY EVAL ~~~~~~~~");

    FirebaseDatabase db = FirebaseDatabase.instance;
    db.useDatabaseEmulator("127.0.0.1", 9000);

    List<int> times = [];
    List<Motorcycle> motorcycles = [];
    int sum = 0;

    for (int i = 0; i < 100; i++) {
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

    List<Future> futures = [];
    for (int i = 0; i < motorcycles.length; i++) {
      DatabaseReference ref = db.ref("Motorcycle/${motorcycles[i].id}");
      futures.add(ref.set(motorcycles[i].toMap()));
    }
    await Future.wait(futures);

    stopwatch.stop();
    times.add(stopwatch.elapsedMicroseconds);
    sum += stopwatch.elapsedMicroseconds;
    Future.delayed(Duration(milliseconds: 100));

    print("Times RDB API: $times");

  }

}