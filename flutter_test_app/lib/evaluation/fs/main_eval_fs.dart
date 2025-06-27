import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:flutter/material.dart';

import '../../generated/firestorm_models.dart';
import 'create/eval_create_firestorm.dart';
import 'create/eval_create_fsapi.dart';
import 'create/eval_create_many_firestorm.dart';
import 'create/eval_create_many_fsapi.dart';
import 'delete/eval_delete_firestorm.dart';
import 'delete/eval_delete_fsapi.dart';
import 'delete/eval_delete_many_firestorm.dart';
import 'delete/eval_delete_many_fsapi.dart';
import 'get/eval_get_firestorm.dart';
import 'get/eval_get_fsapi.dart';
import 'get/eval_get_many_firestorm.dart';
import 'get/eval_get_many_fsapi.dart';
import 'update/eval_update_firestorm.dart';
import 'update/eval_update_fsapi.dart';
import 'update/eval_update_many_firestorm.dart';
import 'update/eval_update_many_fsapi.dart';

main() async {

  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  await FS.init();
  FS.useEmulator("127.0.0.1", 8080);
  registerClasses();

  //Evaluations:
  CreateEvaluationAPI createEvaluationAPI = CreateEvaluationAPI();
  CreateEvaluationFS createEvaluationFS = CreateEvaluationFS();
  GetEvaluationAPI getEvaluationAPI = GetEvaluationAPI();
  GetEvaluationFS getEvaluationFS = GetEvaluationFS();
  UpdateEvaluationAPI updateEvaluationAPI = UpdateEvaluationAPI();
  UpdateEvaluationFS updateEvaluationFS = UpdateEvaluationFS();
  DeleteEvaluationAPI deleteEvaluationAPI = DeleteEvaluationAPI();
  DeleteEvaluationFS deleteEvaluationFS = DeleteEvaluationFS();

  CreateManyEvaluationAPI createManyEvaluationAPI = CreateManyEvaluationAPI();
  CreateManyEvaluationFirestorm createManyEvaluationFirestorm = CreateManyEvaluationFirestorm();
  GetManyEvaluationAPI getManyEvaluationAPI = GetManyEvaluationAPI();
  GetManyEvaluationFS getManyEvaluationFS = GetManyEvaluationFS();
  UpdateManyEvaluationAPI updateManyEvaluationAPI = UpdateManyEvaluationAPI();
  UpdateManyEvaluationFS updateManyEvaluationFS = UpdateManyEvaluationFS();
  DeleteManyEvaluationAPI deleteManyEvaluationAPI = DeleteManyEvaluationAPI();
  DeleteManyEvaluationFS deleteManyEvaluationFS = DeleteManyEvaluationFS();

  //Run:

  //create
  // createEvaluationAPI.run();
  // createEvaluationFS.run();

  //get
  // getEvaluationAPI.run();
  // getEvaluationFS.run();

  //update
  // updateEvaluationAPI.run();
  // updateEvaluationFS.run();

  //delete
  // deleteEvaluationAPI.run();
  // deleteEvaluationFS.run();

  //create many
  // createManyEvaluationAPI.run();
  // createManyEvaluationFirestorm.run();

  //get many
  // getManyEvaluationAPI.run();
  // getManyEvaluationFS.run();

  //update many
  // updateManyEvaluationAPI.run();
  // updateManyEvaluationFS.run();

  //delete many
  // deleteManyEvaluationAPI.run();
  // deleteManyEvaluationFS.run();

  runApp(Container());
}