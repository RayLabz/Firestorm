import 'dart:async';

import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/firestorm.dart';
import 'package:firestorm/rdb/helpers/rdb_deserialization_helper.dart';
import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
import 'package:flutter_test_app/evaluation/data/motorcycle.dart';


class DeleteEvaluationRDBAPI extends EvaluationRuntime {

  @override
  run({List<String>? args}) async {

    print("~~~~~~~~ RDB API DELETE EVAL ~~~~~~~~");

    FirebaseDatabase db = FirebaseDatabase.instance;
    db.useDatabaseEmulator("127.0.0.1", 9000);

    List<String> ids = [];
    List<Motorcycle> motorcycles = [];


    //Create 100 randomly generated items:
    for (int i = 0; i < 101; i++) { //exclude first call as warmup call
      Motorcycle motorcycle = Motorcycle(
          Firestorm.randomID(),
          "Suzuki",
          "Bandit",
          "Cruiser"
      );
      ids.add(motorcycle.id);
      motorcycles.add(motorcycle);
      DatabaseReference ref = db.ref("Motorcycle/${motorcycle.id}");
      await ref.set(motorcycle.toMap());
      motorcycle.brand = "Ducati";
      motorcycle.model = "Monster";
    }

    List<int> times = [];
    int sum = 0;

    for (int i = 0; i < 101; i++) { //exclude first call as warmup call
      // Motorcycle motorcycle = Motorcycle(
      //     Firestorm.randomID(),
      //     "Suzuki",
      //     "Bandit",
      //     "Cruiser"
      // );
      Stopwatch stopwatch = Stopwatch();
      stopwatch.start();

      DatabaseReference ref = db.ref("Motorcycle/${ids[i]}");
      await ref.remove();

      stopwatch.stop();
      times.add(stopwatch.elapsedMicroseconds);
      sum += stopwatch.elapsedMicroseconds;
      Future.delayed(Duration(milliseconds: 100));
    }

    print("Times RDB API: $times");

  }

}