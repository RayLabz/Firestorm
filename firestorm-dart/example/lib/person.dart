import 'package:firestorm_flutter_example/test.dart';

@Reflector()
class Person {

  String? id;
  String? firstname;
  String? lastname;
  int? value;

  Person(this.id, this.firstname, this.lastname, this.value);

  Person.fs({this.id, this.firstname, this.lastname, this.value});

}