// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $OBJECT_UPDATE_ONE() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//   ComputingStudent student = ComputingStudent.generateRandomStudent();
//
//   //TEST OBJECT_UPDATE_1 - Firestore API - Update one object, Non-blocking
//   fs.collection('Student').doc(student.id).update(student.toMap()).then((_) {
//     print('Student updated successfully');
//   }).catchError((error) {
//     print('Failed to update student: $error');
//   });
//
//   //TEST OBJECT_UPDATE_1 - Firestorm FS - Update one object, Non-blocking
//   FS.update.one(student).then((_) {
//     print('Student updated successfully with Firestorm FS');
//   }).catchError((error) {
//     print('Failed to update student with Firestorm FS: $error');
//   });
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_UPDATE_2 - Firestore API - Update one object, Blocking
//   try {
//     await fs.collection('Student').doc(student.id).update(student.toMap());
//     print('Student updated successfully');
//   } catch (error) {
//     print('Failed to update student: $error');
//   }
//
//   //TEST OBJECT_UPDATE_2 - Firestorm FS - Update one object, Blocking
//   try {
//     await FS.update.one(student);
//     print('Student updated successfully with Firestorm FS');
//   } catch (error) {
//     print('Failed to update student with Firestorm FS: $error');
//   }
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_UPDATE_3 - Firestore API - Update one object, Blocking, no error checking
//   await fs.collection('Student').doc(student.id).update(student.toMap());
//
//   //TEST OBJECT_UPDATE_3 - Firestorm FS - Update one object, Blocking, no error checking
//   await FS.update.one(student);
//
// }