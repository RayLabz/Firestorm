// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:firestorm/fs/queries/fs_query_result.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $PAGINATION() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//
//   //TEST PAGINATION_1 - Firestore API - Pagination, Non-blocking
//   fs.collection('Student').orderBy('name').limit(10).get().then((QuerySnapshot snapshot) {
//     if (snapshot.docs.isNotEmpty) {
//       List<ComputingStudent> students = snapshot.docs.map((doc) => ComputingStudent.fromMap(doc.data() as Map<String, dynamic>)).toList();
//       print('Students retrieved successfully: $students');
//     } else {
//       print('No students found');
//     }
//   }).catchError((error) {
//     print('Failed to retrieve students: $error');
//   });
//
//   //TEST PAGINATION_1 - Firestorm FS - Pagination, Non-blocking
//   FS.paginate<ComputingStudent>().next().then((value) {
//     if (value.items.isNotEmpty) {
//       print('Students retrieved successfully with Firestorm FS: ${value.items}');
//     } else {
//       print('No students found with Firestorm FS');
//     }
//   }).catchError((error) {
//     print('Failed to retrieve students with Firestorm FS: $error');
//   });
//
//   //----------------------------------------------------------------------------
//   //TEST PAGINATION_2 - Firestore API - Pagination, Blocking
//   try {
//     QuerySnapshot snapshot = await fs.collection('Student').orderBy('name').limit(10).get();
//     if (snapshot.docs.isNotEmpty) {
//       List<ComputingStudent> students = snapshot.docs.map((doc) => ComputingStudent.fromMap(doc.data() as Map<String, dynamic>)).toList();
//       print('Students retrieved successfully: $students');
//     } else {
//       print('No students found');
//     }
//   } catch (error) {
//     print('Failed to retrieve students: $error');
//   }
//
//   //TEST PAGINATION_2 - Firestorm FS - Pagination, Blocking
//   try {
//     FSQueryResult<ComputingStudent> result = await FS.paginate<ComputingStudent>().next();
//     if (result.items.isNotEmpty) {
//       print('Students retrieved successfully with Firestorm FS: ${result.items}');
//     } else {
//       print('No students found with Firestorm FS');
//     }
//   } catch (error) {
//     print('Failed to retrieve students with Firestorm FS: $error');
//   }
//
//   //----------------------------------------------------------------------------
//
//   //TEST PAGINATION_3 - Firestore API - Pagination, Blocking, no error checking
//   QuerySnapshot snapshot = await fs.collection('Student').orderBy('name').limit(10).get();
//   if (snapshot.docs.isNotEmpty) {
//     List<ComputingStudent> students = snapshot.docs.map((doc) => ComputingStudent.fromMap(doc.data() as Map<String, dynamic>)).toList();
//     print('Students retrieved successfully: $students');
//   } else {
//     print('No students found');
//   }
//
//   //TEST PAGINATION_3 - Firestorm FS - Pagination, Blocking, no error checking
//   FSQueryResult<ComputingStudent> result = await FS.paginate<ComputingStudent>().next();
//   if (result.items.isNotEmpty) {
//     print('Students retrieved successfully with Firestorm FS: ${result.items}');
//   } else {
//     print('No students found with Firestorm FS');
//   }
//
// }