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
  List<Person> persons = [];

  for (int i = 0; i < 10; i++) {
    Person p = Person(
      "$i",
      "John $i",
      "Doe $i",
      20 + i,
      1.8 + i,
      true,
      ["AAA"],
    );
    persons.add(p);
  }

  print(persons);

  await LS.create.one(persons[0]);
  // await LS.create.many(persons.sublist(1));
  //
  // Person? pGet = await LS.get.one<Person>("1");
  // print(pGet);
  //
  // List<Person> personsGet = await LS.get.many<Person>(["1", "2", "5"]);
  // print(personsGet);
  //
  // persons[0].firstname = "Nicos";
  // await LS.update.one(persons[0]);
  //
  // await LS.delete.one("1");
  // await LS.delete.many(["2", "3"]);
  //
  // List<Person> personsList = await LS.list.allOfClass<Person>(Person);
  // print(personsList);
  //
  // bool exists = await LS.exists.one<Person>("1");
  // print(exists);

  print("FINISHED");
}





  runApp(Container());
}