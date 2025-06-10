import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/widgets.dart';
import 'package:uuid/uuid.dart';

typedef Serializer = Map<String, dynamic> Function(dynamic); //Used to dynamically serialize objects
typedef Deserializer = dynamic Function(Map<String, dynamic> map); //Used to dynamically deserialize objects

class Firestorm {

  /// Returns a random ID using UUID v8.
  static String randomID() {
    return const Uuid().v8();
  }

}