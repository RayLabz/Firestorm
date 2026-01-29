import 'package:firestorm/annotations/firestorm_object.dart';

//CLASS WITH BOTH NAMED AND POSITIONAL PARAMETERS

@FirestormObject()
class Contact {
  final String id;
  final String name;
  final int age;
  final String? email;

  Contact(this.id, this.name, {required this.age, this.email});
}
