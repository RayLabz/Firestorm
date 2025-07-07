import 'package:firestorm/fs/fs.dart';
import 'package:firestorm/rdb/rdb.dart';
import 'package:firestorm_demonstrator/contacts_app.dart';
import 'package:flutter/material.dart';

import 'generated/firestorm_models.dart';

void main () async {
  WidgetsFlutterBinding.ensureInitialized(); //Ensure Flutter is initialized

  await FS.init(); //Initialize Firestore
  await RDB.init(); //Initialize Realtime Database

  //Use emulators:
  // FS.useEmulator("localhost", 8080);
  // RDB.useEmulator("localhost", 9000);

  registerClasses(); //Register classes

  runApp(ContactsApp()); //Run the app
}