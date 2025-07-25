import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/firestorm.dart';
import 'package:firestorm/rdb/rdb.dart';
import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
import 'package:flutter_test_app/evaluation/data/motorcycle.dart';


class CreateManyEvaluationFRDB extends EvaluationRuntime {

  @override
  run({List<String>? args}) async {

    print("~~~~~~~~ FIRESTORM CREATE MANY EVAL ~~~~~~~~");

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

    await RDB.create.many(motorcycles);

    stopwatch.stop();
    times.add(stopwatch.elapsedMicroseconds);
    sum += stopwatch.elapsedMicroseconds;
    Future.delayed(Duration(milliseconds: 100));

    print("Times Firestorm: $times");

  }

}