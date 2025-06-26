import 'package:firestorm/fs/fs.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test_app/evaluation/evaluation.dart';

import '../generated/firestorm_models.dart';

class CreateEvaluation extends EvaluationRuntime {



  @override
  setup() {
    WidgetsFlutterBinding.ensureInitialized();
    FS.init();
    FS.useEmulator("127.0.0.1", 8080);
    registerClasses();
  }

  @override
  run(List<String>? args) {
    print("Eval start");
  }

  @override
  shutdown() {
    print("Evaluation end");
  }

}