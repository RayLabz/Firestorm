// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $OBJECT_CREATE_ONE() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//   ComputingStudent student = ComputingStudent.generateRandomStudent();
//
//   //TEST OBJECT_CREATE_1 - Firestore API - Create one object, Non-blocking
//   fs.collection('Student').doc(student.id).set(student.toMap()).then((_) {
//     print('Student created successfully');
//   }).catchError((error) {
//     print('Failed to create student: $error');
//   });
//
//   //TEST OBJECT_CREATE_1 - Firestorm FS - Create one object, Non-blocking
//   FS.create.one(student).then((_) {
//     print('Student created successfully with Firestorm FS');
//   }).catchError((error) {
//     print('Failed to create student with Firestorm FS: $error');
//   });
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_CREATE_2 - Firestore API - Create one object, Blocking
//   try {
//     await fs.collection('Student').doc(student.id).set(student.toMap());
//     print('Student created successfully');
//   } catch (error) {
//     print('Failed to create student: $error');
//   }
//
//   //TEST OBJECT_CREATE_2 - Firestorm FS - Create one object, Blocking
//   try {
//     await FS.create.one(student);
//     print('Student created successfully with Firestorm FS');
//   } catch (error) {
//     print('Failed to create student with Firestorm FS: $error');
//   }
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_CREATE_3 - Firestore API - Create one object, Blocking, no error checking
//   await fs.collection('Student').doc(student.id).set(student.toMap());
//
//   //TEST OBJECT_CREATE_3 - Firestorm FS - Create one object, Blocking, no error checking
//   await FS.create.one(student);
//
// }