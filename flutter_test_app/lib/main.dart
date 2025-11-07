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

  await FS.init();
  await RDB.init();
  registerClasses();

  //TODO - Test create, get, update, delete, list, exist...

  // String id = Firestorm.randomID();
  // Person p = Person(id, "Nicos", "K", 1, 1, true, []);
  // await LS.create.one(p);
  // print("Person created");
  //
  // Person? load = await LS.get.one(id);
  // print("Person loaded: ${load?.firstname}");

  List<Person> persons = [
    Person("1", "Nicos", "K", 1, 1, true, []),
    Person("2", "Nicos2", "K", 1, 1, true, []),
    Person("3", "Nicos3", "K", 1, 1, true, []),
    Person("4", "Nicos4", "K", 1, 1, true, []),
    Person("5", "Nicos5", "K", 1, 1, true, []),
    Person("6", "Nicos6", "K", 1, 1, true, []),
    Person("7", "Nicos7", "K", 1, 1, true, []),
    Person("8", "Nicos8", "K", 1, 1, true, []),
    Person("9", "Nicos9", "K", 1, 1, true, []),
    Person("10", "Nicos10", "K", 1, 1, true, []),
    Person("11", "Nicos11", "K", 1, 1, true, []),
  ];


  await FS.create.many(persons);

  FS.listen.toObjects<Person>(
    persons,
    onChange: (object) {
      print("Changed: $object.");
    },
  );




  runApp(Container());
}