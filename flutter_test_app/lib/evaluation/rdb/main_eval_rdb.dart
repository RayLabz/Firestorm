import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/rdb/rdb.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test_app/evaluation/rdb/create/eval_create_many_rdb.dart';
import 'package:flutter_test_app/evaluation/rdb/get/eval_get_rdb.dart';
import 'package:flutter_test_app/evaluation/rdb/get/eval_get_rdbapi.dart';
import 'package:flutter_test_app/evaluation/rdb/update/eval_update_many_rdb.dart';
import 'package:flutter_test_app/evaluation/rdb/update/eval_update_many_rdbapi.dart';
import 'package:flutter_test_app/evaluation/rdb/update/eval_update_rdb.dart';
import 'package:flutter_test_app/evaluation/rdb/update/eval_update_rdbapi.dart';

import '../../generated/firestorm_models.dart';
import 'create/eval_create_many_rdbapi.dart';
import 'create/eval_create_rdb.dart';
import 'create/eval_create_rdbapi.dart';
import 'delete/eval_delete_many_rdb.dart';
import 'delete/eval_delete_many_rdbapi.dart';
import 'delete/eval_delete_rdb.dart';
import 'delete/eval_delete_rdbapi.dart';
import 'get/eval_get_many_rdb.dart';
import 'get/eval_get_many_rdbapi.dart';

main() async {

  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  await RDB.init();
  RDB.useEmulator("127.0.0.1", 8080);
  registerClasses();

  //Evaluations:
  CreateEvaluationRDBAPI createEvaluationRDBAPI = CreateEvaluationRDBAPI();
  CreateEvaluationFRDB createEvaluationFRDB = CreateEvaluationFRDB();
  GetEvaluationFRDB getEvaluationFRDB = GetEvaluationFRDB();
  GetEvaluationRDBAPI getEvaluationRDBAPI = GetEvaluationRDBAPI();
  UpdateEvaluationRDBAPI updateEvaluationRDBAPI = UpdateEvaluationRDBAPI();
  UpdateEvaluationFRDB updateEvaluationFRDB = UpdateEvaluationFRDB();
  DeleteEvaluationRDBAPI deleteEvaluationRDBAPI = DeleteEvaluationRDBAPI();
  DeleteEvaluationFRDB deleteEvaluationFRDB = DeleteEvaluationFRDB();

  CreateManyEvaluationRDBAPI createManyEvaluationRDBAPI = CreateManyEvaluationRDBAPI();
  CreateManyEvaluationFRDB createManyEvaluationFRDB = CreateManyEvaluationFRDB();
  GetManyEvaluationRDBAPI getManyEvaluationRDBAPI = GetManyEvaluationRDBAPI();
  GetManyEvaluationFRDB getManyEvaluationFRDB = GetManyEvaluationFRDB();
  UpdateManyEvaluationRDBAPI updateManyEvaluationRDBAPI = UpdateManyEvaluationRDBAPI();
  UpdateManyEvaluationFRDB updateManyEvaluationFRDB = UpdateManyEvaluationFRDB();
  DeleteManyEvaluationRDBAPI deleteManyEvaluationRDBAPI = DeleteManyEvaluationRDBAPI();
  DeleteManyEvaluationFRDB deleteManyEvaluationFRDB = DeleteManyEvaluationFRDB();

  //Run:

  //create
  // await createEvaluationRDBAPI.run();
  // await createEvaluationFRDB.run();

  //get
  // await getEvaluationRDBAPI.run();
  // await getEvaluationFRDB.run();

  //update
  // await updateEvaluationRDBAPI.run();
  // await updateEvaluationFRDB.run();

  //delete
  // await deleteEvaluationRDBAPI.run();
  // await deleteEvaluationFRDB.run();

  //create many
  // await createManyEvaluationRDBAPI.run();
  // await createManyEvaluationFRDB.run();

  //get many
  // await getManyEvaluationRDBAPI.run();
  // await getManyEvaluationFRDB.run();

  //update many
  // await updateManyEvaluationRDBAPI.run();
  // await updateManyEvaluationFRDB.run();

  //delete many
  await deleteManyEvaluationRDBAPI.run();
  await deleteManyEvaluationFRDB.run();

  runApp(Container());
}