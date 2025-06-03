import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/widgets.dart';

typedef Serializer = Map<String, dynamic> Function(dynamic); //Used to dynamically serialize objects

class Firestorm {

  static Future<void> init() async {
    WidgetsFlutterBinding.ensureInitialized();
    await Firebase.initializeApp();
    //Initialize data
  }

}