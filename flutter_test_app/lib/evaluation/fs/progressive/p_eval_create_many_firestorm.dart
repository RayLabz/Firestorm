import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/firestorm.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:flutter_test_app/evaluation/evaluation_runtime.dart';
import 'package:flutter_test_app/evaluation/data/motorcycle.dart';


class ProgressiveCreateManyEvaluationFirestorm extends EvaluationRuntime {

  @override
  run({List<String>? args}) async {

    print("~~~~~~~~ FIRESTORM P-CREATE MANY EVAL ~~~~~~~~");

    FirebaseFirestore db = FirebaseFirestore.instance;
    db.useFirestoreEmulator("127.0.0.1", 8080);

    final int startingNumber = 0;
    final int endingNumber = 500;
    final int increment = 50;

    Map<int, int> averageTimes = {};

    for (int currentNumber = startingNumber; currentNumber <= endingNumber; currentNumber += increment) {

      List<int> times = [];
      List<Motorcycle> motorcycles = [];
      int sum = 0;

      for (int i = 0; i < currentNumber; i++) {
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

      await FS.create.many(motorcycles);

      stopwatch.stop();
      times.add(stopwatch.elapsedMilliseconds);
      sum += stopwatch.elapsedMilliseconds;
      averageTimes[currentNumber] = sum ~/ times.length;

      // print("Times Firestorm for $i objects:\n\n $times");

      Future.delayed(Duration(milliseconds: 100));

    }

    print("FIRESTORM AVERAGE TIMES:");
    for (int i = startingNumber; i <= endingNumber; i += increment) {
      print("$i\t${averageTimes[i]}\t\t(ms)");
    }

  }

}