import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/firestorm.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
import 'package:flutter_test_app/evaluation/data/motorcycle.dart';


class UpdateManyEvaluationFS extends EvaluationRuntime {

  @override
  run({List<String>? args}) async {

    print("~~~~~~~~ FIRESTORM UPDATE MANY EVAL ~~~~~~~~");

    FirebaseFirestore db = FirebaseFirestore.instance;
    db.useFirestoreEmulator("127.0.0.1", 8080);

    List<int> times = [];
    List<Motorcycle> motorcycles = [];
    List<String> ids = [];
    int sum = 0;

    for (int i = 0; i < 100; i++) { //exclude first call as warmup call
      Motorcycle motorcycle = Motorcycle(
          Firestorm.randomID(),
          "Suzuki",
          "Bandit",
          "Cruiser"
      );
      motorcycles.add(motorcycle);
      ids.add(motorcycle.id);
    }
    await FS.create.many(motorcycles);

    for (Motorcycle m in motorcycles) {
      m.brand = "Honda";
      m.model = "CBR";
      m.type = "Sport";
    }

    Stopwatch stopwatch = Stopwatch();
    stopwatch.start();

    await FS.update.many(motorcycles);

    stopwatch.stop();
    times.add(stopwatch.elapsedMicroseconds);
    sum += stopwatch.elapsedMicroseconds;
    Future.delayed(Duration(milliseconds: 100));

    print("Times FS: $times");

  }

}