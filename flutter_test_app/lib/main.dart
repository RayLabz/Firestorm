import 'dart:async';
import 'dart:io';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/firestorm.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:firestorm/fs/queries/fs_paginator.dart';
import 'package:firestorm/fs/queries/fs_query_result.dart';
import 'package:firestorm/ls/ls.dart';
import 'package:firestorm/rdb/queries/rdb_filterable.dart';
import 'package:firestorm/rdb/queries/rdb_query_result.dart';
import 'package:firestorm/rdb/rdb.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter_test_app/generated/firestorm_models.dart';
import 'package:flutter_test_app/person.dart';

import 'address.dart';
import 'computing_student.dart';

main() async {
  WidgetsFlutterBinding.ensureInitialized();

  print("STARTED");

  await FS.init();
  await RDB.init();
  registerClasses();

  List<Person> p = [];

  for (int i = 0; i < 100; i++) {
    final person = Person('id_$i', 'First$i', 'Last$i', 20 + i, 1.5 * i, i % 2 == 0, ['friend${i+1}', 'friend${i+2}']);
    p.add(person);
  }

  await RDB.create.many(p, subcollection: "aha");
  print("Write complete");

  var list = await RDB.list.allOfClass<Person>(Person, subcollection: "aha");
  print("Got list of ${list.length} people");

  print("PRess enter to delete");

  print("Deleting...");
  await RDB.delete.all(Person, iAmSure: true, subcollection: "aha");


  print("FINISHED");



  runApp(Container());
}