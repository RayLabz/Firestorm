import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:firestorm/fs/queries/fs_paginator.dart';
import 'package:firestorm/fs/queries/fs_query_result.dart';
import 'package:firestorm/rdb/queries/rdb_query_result.dart';
import 'package:firestorm/rdb/rdb.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';

import 'address.dart';
import 'computing_student.dart';

main() async {
  WidgetsFlutterBinding.ensureInitialized();

  await FS.init();
  await RDB.init();
  registerClasses();


  FirebaseFirestore fs;
  FirebaseDatabase db;

  // var snapshot = await fs.collection("Person")
  //   .doc(person.id)
  //   .get();
  //
  // Person? p;
  //
  // if (snapshot.exists) {
  //   p = Person(
  //       snapshot.get("id") as String,
  //       snapshot.get("name") as String,
  //       snapshot.get("age") as int
  //   );
  // }

  var snapshot = await db.ref("Person")
    .child(person.id)
    .get();

  Person? p;

  if (snapshot.exists) {
    final data = snapshot.value as Map<dynamic, dynamic>;
    p = Person(
      data["id"] as String,
      data["name"] as String,
      data["age"] as int,
    );
  }



  runApp(Container());
}