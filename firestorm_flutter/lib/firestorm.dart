import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/widgets.dart';
import 'package:uuid/uuid.dart';

class Firestorm {

  /// Initializes Firestorm and Firebase.
  static Future<void> init() async {
    await Firebase.initializeApp();
    //Initialize data
  }

  /// Returns a random ID using UUID v8.
  static String randomID() {
    return const Uuid().v8();
  }

}