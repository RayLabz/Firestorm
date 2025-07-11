// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $OBJECT_DELETE_ONE() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//   ComputingStudent student = ComputingStudent.generateRandomStudent();
//
//   //TEST OBJECT_DELETE_1 - Firestore API - Delete one object, Non-blocking
//   fs.collection('Student').doc(student.id).delete().then((_) {
//     print('Student deleted successfully');
//   }).catchError((error) {
//     print('Failed to delete student: $error');
//   });
//
//   //TEST OBJECT_DELETE_1 - Firestorm FS - Delete one object, Non-blocking
//   FS.delete.one(student).then((_) {
//     print('Student deleted successfully with Firestorm FS');
//   }).catchError((error) {
//     print('Failed to delete student with Firestorm FS: $error');
//   });
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_DELETE_2 - Firestore API - Delete one object, Blocking
//   try {
//     await fs.collection('Student').doc(student.id).delete();
//     print('Student deleted successfully');
//   } catch (error) {
//     print('Failed to delete student: $error');
//   }
//
//   //TEST OBJECT_DELETE_2 - Firestorm FS - Delete one object, Blocking
//   try {
//     await FS.delete.one(student);
//     print('Student deleted successfully with Firestorm FS');
//   } catch (error) {
//     print('Failed to delete student with Firestorm FS: $error');
//   }
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_DELETE_3 - Firestore API - Delete one object, Blocking, no error checking
//   await fs.collection('Student').doc(student.id).delete();
//
//   //TEST OBJECT_DELETE_3 - Firestorm FS - Delete one object, Blocking, no error checking
//   await FS.delete.one(student);
//
// }