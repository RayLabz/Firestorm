import 'package:firestorm/annotations/firestorm_object.dart';

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

  @override
  String toString() {
    return 'Person{id: $id, firstname: $firstname, lastname: $lastname, age: $age, height: $height, isEmployed: $isEmployed, friends: $friends}';
  }



}