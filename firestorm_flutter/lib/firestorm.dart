import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/widgets.dart';
import 'package:uuid/uuid.dart';

class Firestorm {

  /// Returns a random ID using UUID v8.
  static String randomID() {
    return const Uuid().v8();
  }

}