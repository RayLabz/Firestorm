import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/widgets.dart';

class Firestorm {

  static Future<void> init() async {
    WidgetsFlutterBinding.ensureInitialized();
    await Firebase.initializeApp();
    //Initialize data
  }

}