// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $QUERY() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//
//   //TEST QUERY_1 - Firestore API - Query one object, Non-blocking
//   fs.collection('Student')
//       .where('age', isGreaterThan: 20)
//       .get().then((QuerySnapshot querySnapshot) {
//     if (querySnapshot.docs.isNotEmpty) {
//       for (var doc in querySnapshot.docs) {
//         ComputingStudent student = ComputingStudent.fromMap(doc.data() as Map<String, dynamic>);
//         print('Student retrieved successfully: $student');
//       }
//     } else {
//       print('No students found with age greater than 20');
//     }
//   }).catchError((error) {
//     print('Failed to retrieve students: $error');
//   });
//
//   //TEST QUERY_1 - Firestorm FS - Query one object, Non-blocking
//   FS.list.filter<ComputingStudent>(ComputingStudent)
//       .whereEqualTo('age', 20)
//       .fetch().then((result) {
//     if (result.items.isNotEmpty) {
//       for (var student in result.items) {
//         print('Student retrieved successfully with Firestorm FS: $student');
//       }
//     } else {
//       print('No students found with age greater than 20');
//     }
//   }).catchError((error) {
//     print('Failed to retrieve students with Firestorm FS: $error');
//   });
//
//   //----------------------------------------------------------------------------
//
//   //TEST QUERY_2 - Firestore API - Query one object, Blocking
//   try {
//     QuerySnapshot querySnapshot = await fs.collection('Student')
//         .where('age', isGreaterThan: 20)
//         .get();
//     if (querySnapshot.docs.isNotEmpty) {
//       for (var doc in querySnapshot.docs) {
//         ComputingStudent student = ComputingStudent.fromMap(doc.data() as Map<String, dynamic>);
//         print('Student retrieved successfully: $student');
//       }
//     } else {
//       print('No students found with age greater than 20');
//     }
//   } catch (error) {
//     print('Failed to retrieve students: $error');
//   }
//
//   //TEST QUERY_2 - Firestorm FS - Query one object, Blocking
//   try {
//     var result = await FS.list.filter<ComputingStudent>(ComputingStudent)
//         .whereEqualTo('age', 20)
//         .fetch();
//     if (result.items.isNotEmpty) {
//       for (var student in result.items) {
//         print('Student retrieved successfully with Firestorm FS: $student');
//       }
//     } else {
//       print('No students found with age greater than 20');
//     }
//   } catch (error) {
//     print('Failed to retrieve students with Firestorm FS: $error');
//   }
//
//   //----------------------------------------------------------------------------
//
//   //TEST QUERY_3 - Firestore API - Query one object, Blocking, no error checking
//   QuerySnapshot querySnapshot = await fs.collection('Student')
//       .where('age', isGreaterThan: 20)
//       .get();
//   if (querySnapshot.docs.isNotEmpty) {
//     for (var doc in querySnapshot.docs) {
//       ComputingStudent student = ComputingStudent.fromMap(doc.data() as Map<String, dynamic>);
//       print('Student retrieved successfully: $student');
//     }
//   } else {
//     print('No students found with age greater than 20');
//   }
//
//   //TEST QUERY_3 - Firestorm FS - Query one object, Blocking, no error checking
//   var result = await FS.list.filter<ComputingStudent>(ComputingStudent)
//       .whereEqualTo('age', 20)
//       .fetch();
//   if (result.items.isNotEmpty) {
//     for (var student in result.items) {
//       print('Student retrieved successfully with Firestorm FS: $student');
//     }
//   } else {
//     print('No students found with age greater than 20');
//   }
//
// }