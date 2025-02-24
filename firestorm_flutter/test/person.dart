import 'package:firestorm/annotation/firestorm_object.dart';

@FirestormObject()
class Person {

  String id;
  String firstname;
  String lastname;
  int age;
  double height;
  bool isEmployed;
  List<String> friends;

  Person(this.id, this.firstname, this.lastname, this.age, this.height,
      this.isEmployed, this.friends);

}