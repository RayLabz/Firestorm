import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
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

  //TODO - Test create, get, update, delete, list, exist...


  //Create an array of 5 person:
  // List<Person> persons = [];
  //
  // for (int i = 0; i < 10; i++) {
  //   Person p = Person(
  //     Firestorm.randomID(),
  //     "John$i",
  //     "Doe$i",
  //     25+i,
  //     1.8,
  //     true,
  //     ["Friend1", "Friend2"]
  //   );
  //   persons.add(p);
  // }
  //
  //
  // await FS.create.many(
  //     persons,
  //     subcollection: "SpecialPeople"
  // );

  FS.listen.toType<Person>(
    subcollection: "SpecialPeople",
    Person,
    onCreate: (object) {
      print("Created $object");
    },
    onChange: (object) {
      print("Updated $object");
    },
    onDelete: (object) {
      print("Deleted $object");
    },
  );



  runApp(Container());
}