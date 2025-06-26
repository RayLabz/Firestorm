import 'dart:async';

abstract class EvaluationRuntime {

  ///Sets up the environment to run the evaluation.
  setup() {}

  ///Runs the evaluation.
  run(List<String>? args);

  ///Shuts down the environment.
  shutdown() {}

}