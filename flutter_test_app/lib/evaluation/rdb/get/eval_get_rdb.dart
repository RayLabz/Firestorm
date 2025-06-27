import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/firestorm.dart';
import 'package:firestorm/rdb/rdb.dart';
import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
import 'package:flutter_test_app/evaluation/data/motorcycle.dart';


class GetEvaluationFRDB extends EvaluationRuntime {

  @override
  run({List<String>? args}) async {

    print("~~~~~~~~ RDB API GET EVAL ~~~~~~~~");

    FirebaseDatabase db = FirebaseDatabase.instance;
    db.useDatabaseEmulator("127.0.0.1", 9000);

    List<String> ids = [];


    //Create 100 randomly generated items:
    for (int i = 0; i < 101; i++) { //exclude first call as warmup call
      Motorcycle motorcycle = Motorcycle(
          Firestorm.randomID(),
          "Suzuki",
          "Bandit",
          "Cruiser"
      );
      ids.add(motorcycle.id);
      DatabaseReference ref = db.ref("Motorcycle/${motorcycle.id}");
      await ref.set(motorcycle.toMap());
    }

    List<int> times = [];
    int sum = 0;

    for (int i = 0; i < 101; i++) { //exclude first call as warmup call
      Motorcycle motorcycle = Motorcycle(
          Firestorm.randomID(),
          "Suzuki",
          "Bandit",
          "Cruiser"
      );
      Stopwatch stopwatch = Stopwatch();
      stopwatch.start();

      Motorcycle? m = await RDB.get.one<Motorcycle>(ids[i]);

      stopwatch.stop();
      times.add(stopwatch.elapsedMicroseconds);
      sum += stopwatch.elapsedMicroseconds;
      Future.delayed(Duration(milliseconds: 100));
    }

    print("Times Firestorm: $times");

  }

}