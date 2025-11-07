// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $LISTENERS() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//   ComputingStudent student = ComputingStudent.generateRandomStudent();
//
//   //TEST LISTENERS_1 - Firestore API - Listen to changes in a document, Non-blocking
//   fs.collection('Student').doc('some_student_id').snapshots().listen((DocumentSnapshot document) {
//     if (document.exists) {
//       ComputingStudent student = ComputingStudent.fromMap(document.data() as Map<String, dynamic>);
//       print('Student data changed: $student');
//     } else {
//       print('No such student exists');
//     }
//   }, onError: (error) {
//     print('Failed to listen to student changes: $error');
//   });
//
//   //TEST LISTENERS_1 - Firestorm FS - Listen to changes in a document, Non-blocking
//   FS.listen.toObject(
//     student,
//     onCreate: (onCreate) {
//       print('Student created: $onCreate');
//     },
//     onChange: (onUpdate) {
//       print('Student updated: $onUpdate');
//     },
//     onDelete: () {
//       print('Student deleted');
//     },
//   );

//
// }