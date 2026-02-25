// import 'dart:math';
//
// import 'package:firestorm/annotations/exclude.dart';
// import 'package:firestorm/annotations/firestorm_object.dart';
// import 'package:flutter/material.dart';
// import 'package:flutter_test_app/student.dart';
//
// import 'address.dart';
// import 'custom_color.dart';
//
// @FirestormObject()
// class ComputingStudent extends Student {
//
//   @Exclude()
//   String? password;
//   String pathway;
//   Address address;
//   Map<String, int> grades;
//   CustomColor favoriteColor;
//
//   ComputingStudent(
//       super.id,
//       super.firstname,
//       super.lastname,
//       super.age,
//       super.height,
//       super.isEmployed,
//       super.friends,
//       super.studentID,
//       super.school,
//       this.pathway,
//       this.password,
//       this.address,
//       this.grades,
//       this.favoriteColor
//     );
//
//   static String generateRandomSubject() {
//     final subjects = ["Math", "Science", "History", "Art", "Computer Science"];
//     return subjects[Random().nextInt(subjects.length)];
//   }
//
//   static String generateRandomPathway() {
//     final pathways = ["Software Development", "Data Science", "Cybersecurity", "Web Development", "AI"];
//     return pathways[Random().nextInt(pathways.length)];
//   }
//
//   static String generateRandomSchool() {
//     final schools = ["School of Computing", "School of Engineering", "School of Arts", "School of Science"];
//     return schools[Random().nextInt(schools.length)];
//   }
//
//   static ComputingStudent generateRandomStudent() {
//     final random = Random();
//     final id = random.nextInt(100000).toString();
//     final firstname = "FirstName$id";
//     final lastname = "LastName$id";
//     final age = random.nextInt(10) + 18; // Age between 18 and 27
//     final height = 1.5 + random.nextDouble() * 0.5; // Height between 1.5 and 2.0 meters
//     final isEmployed = random.nextBool();
//     final friends = List.generate(random.nextInt(5), (index) => "Friend$index");
//     final studentID = "G$id";
//     final school = generateRandomSchool();
//     final pathway = generateRandomPathway();
//     final password = "password$id";
//     final address = Address(id, "Street $id", "City $id");
//     final color = CustomColor.values[random.nextInt(CustomColor.values.length)];
//     final grades = {
//       generateRandomSubject(): random.nextInt(100),
//       generateRandomSubject(): random.nextInt(100),
//     };
//
//     return ComputingStudent(
//       id,
//       firstname,
//       lastname,
//       age,
//       height,
//       isEmployed,
//       friends,
//       studentID,
//       school,
//       pathway,
//       password,
//       address,
//       grades,
//       color
//     );
//   }
//
//   static List<ComputingStudent> generateStudents(int numberOfStudents) {
//     return List.generate(numberOfStudents, (index) => generateRandomStudent());
//   }
//
//   @override
//   String toString() {
//     return firstname;
//   }
//
//   @override
//   bool operator ==(Object other) {
//     if (other is! ComputingStudent) {
//       return false;
//     }
//     return id == other.id;
//   }
//
// }