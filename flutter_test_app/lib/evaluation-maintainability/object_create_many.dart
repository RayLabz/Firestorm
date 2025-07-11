// import 'package:cloud_firestore/cloud_firestore.dart';
// import 'package:firestorm/fs/fs.dart';
// import 'package:flutter_test_app/generated/firestorm_models.dart';
// import '../computing_student.dart';
//
// //WARNING - DO NOT EXECUTE THIS FUNCTION - ANALYSIS ONLY
// $OBJECT_CREATE_ONE() async {
//
//   FirebaseFirestore fs = FirebaseFirestore.instance;
//   List<ComputingStudent> students = ComputingStudent.generateStudents(10);
//
//   //TEST OBJECT_CREATE_MANY - Firestore API - Create many objects, Non-blocking
//   WriteBatch batch = fs.batch();
//   for (var student in students) {
//     batch.set(fs.collection('Student').doc(student.id), student.toMap());
//   }
//   batch.commit().then((_) {
//     print('Students created successfully');
//   }).catchError((error) {
//     print('Failed to create students: $error');
//   });
//
//   //TEST OBJECT_CREATE_MANY - Firestorm FS - Create many objects, Non-blocking
//   FS.create.many(students).then((_) {
//     print('Students created successfully with Firestorm FS');
//   }).catchError((error) {
//     print('Failed to create students with Firestorm FS: $error');
//   });
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_CREATE_MANY - Firestore API - Create many objects, Blocking
//   try {
//     WriteBatch batch = fs.batch();
//     for (var student in students) {
//       batch.set(fs.collection('Student').doc(student.id), student.toMap());
//     }
//     await batch.commit();
//     print('Students created successfully');
//   } catch (error) {
//     print('Failed to create students: $error');
//   }
//
//   //TEST OBJECT_CREATE_MANY - Firestorm FS - Create many objects, Blocking
//   try {
//     await FS.create.many(students);
//     print('Students created successfully with Firestorm FS');
//   } catch (error) {
//     print('Failed to create students with Firestorm FS: $error');
//   }
//
//   //----------------------------------------------------------------------------
//
//   //TEST OBJECT_CREATE_MANY - Firestore API - Create many objects, Blocking, no error checking
//   WriteBatch batch = fs.batch();
//   for (var student in students) {
//     batch.set(fs.collection('Student').doc(student.id), student.toMap());
//   }
//   await batch.commit();
//
//   //TEST OBJECT_CREATE_MANY - Firestorm FS - Create many objects, Blocking, no error checking
//   await FS.create.many(students);
//
// }