// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $OBJECT_GET_ONE() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//   ComputingStudent student = ComputingStudent.generateRandomStudent();
//
//   //TEST OBJECT_GET_1 - Firestore API - Get one object, Non-blocking
//   fs.collection('Student').doc(student.id).get().then((DocumentSnapshot document) {
//     if (document.exists) {
//       ComputingStudent retrievedStudent = ComputingStudent.fromMap(document.data() as Map<String, dynamic>);
//       print('Student retrieved successfully: $retrievedStudent');
//     } else {
//       print('No such student exists');
//     }
//   }).catchError((error) {
//     print('Failed to retrieve student: $error');
//   });
//
//   //TEST OBJECT_GET_1 - Firestorm FS - Get one object, Non-blocking
//   FS.get.one<ComputingStudent>(student.id).then((retrievedStudent) {
//     print('Student retrieved successfully with Firestorm FS: $retrievedStudent');
//   }).catchError((error) {
//     print('Failed to retrieve student with Firestorm FS: $error');
//   });
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_GET_2 - Firestore API - Get one object, Blocking
//   try {
//     DocumentSnapshot document = await fs.collection('Student').doc(student.id).get();
//     if (document.exists) {
//       ComputingStudent retrievedStudent = ComputingStudent.fromMap(document.data() as Map<String, dynamic>);
//       print('Student retrieved successfully: $retrievedStudent');
//     } else {
//       print('No such student exists');
//     }
//   } catch (error) {
//     print('Failed to retrieve student: $error');
//   }
//
//   //TEST OBJECT_GET_2 - Firestorm FS - Get one object, Blocking
//   try {
//     ComputingStudent? retrievedStudent = await FS.get.one<ComputingStudent>(student.id);
//     print('Student retrieved successfully with Firestorm FS: $retrievedStudent');
//   } catch (error) {
//     print('Failed to retrieve student with Firestorm FS: $error');
//   }
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_GET_3 - Firestore API - Get one object, Blocking, no error checking
//   DocumentSnapshot document = await fs.collection('Student').doc(student.id).get();
//
//   if (document.exists) {
//     ComputingStudent retrievedStudent = ComputingStudent.fromMap(document.data() as Map<String, dynamic>);
//     print('Student retrieved successfully: $retrievedStudent');
//   } else {
//     print('No such student exists');
//   }
//
//   //TEST OBJECT_GET_3 - Firestorm FS - Get one object, Blocking, no error checking
//   ComputingStudent? retrievedStudent = await FS.get.one<ComputingStudent>(student.id);
//
// }