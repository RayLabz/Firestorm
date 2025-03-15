import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:flutter_test_app/student.dart';

@FirestormObject()
class ComputingStudent extends Student {

  String pathway;

  ComputingStudent(super.id, super.firstname, super.lastname, super.age, super.height, super.isEmployed, super.friends, super.studentID, super.school, this.pathway);

}