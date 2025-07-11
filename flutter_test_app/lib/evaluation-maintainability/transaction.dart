// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $TRANSACTION() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//
//   //TEST TRANSACTION_1 - Firestore API - Transaction, Non-blocking
//   fs.runTransaction((transaction) async {
//     DocumentReference docRef = fs.collection('Student').doc('some_student_id');
//     DocumentSnapshot snapshot = await transaction.get(docRef);
//
//     if (snapshot.exists) {
//       ComputingStudent student = ComputingStudent.fromMap(snapshot.data() as Map<String, dynamic>);
//       // Perform some operation on the student object
//       print('Transaction successful: $student');
//     } else {
//       print('No such student exists');
//     }
//   }).then((_) {
//     print('Transaction completed successfully');
//   }).catchError((error) {
//     print('Failed to complete transaction: $error');
//   });
//
//   //TEST TRANSACTION_1 - Firestorm FS - Transaction, Non-blocking
//   FS.transaction.run((transaction) async {
//     ComputingStudent? student = await transaction.get.one<ComputingStudent>('some_student_id');
//     // Perform some operation on the student object
//     print('Transaction successful with Firestorm FS: $student');
//   }).then((_) {
//     print('Transaction completed successfully with Firestorm FS');
//   }).catchError((error) {
//     print('Failed to complete transaction with Firestorm FS: $error');
//   });
//
//   //----------------------------------------------------------------------------
//
//   //TEST TRANSACTION_2 - Firestore API - Transaction, Blocking
//   try {
//     await fs.runTransaction((transaction) async {
//       DocumentReference docRef = fs.collection('Student').doc('some_student_id');
//       DocumentSnapshot snapshot = await transaction.get(docRef);
//
//       if (snapshot.exists) {
//         ComputingStudent student = ComputingStudent.fromMap(snapshot.data() as Map<String, dynamic>);
//         // Perform some operation on the student object
//         print('Transaction successful: $student');
//       } else {
//         print('No such student exists');
//       }
//     });
//     print('Transaction completed successfully');
//   } catch (error) {
//     print('Failed to complete transaction: $error');
//   }
//
//   //TEST TRANSACTION_2 - Firestorm FS - Transaction, Blocking
//   try {
//     await FS.transaction.run((transaction) async {
//       ComputingStudent? student = await transaction.get.one<ComputingStudent>('some_student_id');
//       // Perform some operation on the student object
//       print('Transaction successful with Firestorm FS: $student');
//     });
//     print('Transaction completed successfully with Firestorm FS');
//   } catch (error) {
//     print('Failed to complete transaction with Firestorm FS: $error');
//   }
//
// }