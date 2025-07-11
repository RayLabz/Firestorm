import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/fs/fs.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test_app/evaluation/fs/progressive/p_eval_create_many_firestorm.dart';
import 'package:flutter_test_app/evaluation/fs/progressive/p_eval_create_many_fsapi.dart';
import 'package:flutter_test_app/evaluation/fs/progressive/p_eval_get_many_firestorm.dart';
import 'package:flutter_test_app/evaluation/fs/progressive/p_eval_get_many_fsapi.dart';

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

  ProgressiveCreateManyEvaluationAPI progressiveCreateManyEvaluationAPI = ProgressiveCreateManyEvaluationAPI();
  ProgressiveCreateManyEvaluationFirestorm progressiveCreateManyEvaluationFirestorm = ProgressiveCreateManyEvaluationFirestorm();

  ProgressiveGetManyEvaluationFSAPI progressiveGetManyEvaluationFSAPI = ProgressiveGetManyEvaluationFSAPI();
  ProgressiveGetManyEvaluationFirestorm progressiveGetManyEvaluationFirestorm = ProgressiveGetManyEvaluationFirestorm();

  //Run:

  //create
  // await createEvaluationAPI.run();
  // await createEvaluationFS.run();

  //get
  // await getEvaluationAPI.run();
  // await getEvaluationFS.run();

  //update
  // await updateEvaluationAPI.run();
  // await updateEvaluationFS.run();

  //delete
  // await deleteEvaluationAPI.run();
  // await deleteEvaluationFS.run();

  //create many
  // await createManyEvaluationAPI.run();
  // await createManyEvaluationFirestorm.run();

  //get many
  // await getManyEvaluationAPI.run();
  // await getManyEvaluationFS.run();

  //update many
  // await updateManyEvaluationAPI.run();
  // await updateManyEvaluationFS.run();

  //delete many
  // await deleteManyEvaluationAPI.run();
  // await deleteManyEvaluationFS.run();

  //progressive evaluation
  // await progressiveCreateManyEvaluationAPI.run();
  // await progressiveCreateManyEvaluationFirestorm.run();

  await progressiveGetManyEvaluationFSAPI.run();
  await progressiveGetManyEvaluationFirestorm.run();

  runApp(Container());
}