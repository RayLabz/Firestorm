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


  runApp(Container());
}