import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:firestorm/firestorm.dart';
import 'package:firestorm_demonstrator/model/address.dart';
import 'package:flutter/cupertino.dart';

@FirestormObject()
class Contact {

  static Map<String, Contact> allContacts = {};

  String id;
  String firstname;
  String lastname;
  String email;
  String phone;
  Address address; // COMPOSITION
  int createdAt;

  Contact({
    required this.id,
    required this.firstname,
    required this.lastname,
    required this.email,
    required this.phone,
    required this.address,
    required this.createdAt,
  });

  static List<String> _firstNames = [
    'John',
    'Jane',
    'Alice',
    'Bob',
    'Charlie',
    'Diana',
    'Ethan',
    'Fiona',
    'George',
    'Hannah'
  ];

  static List<String> _lastNames = [
    'Smith',
    'Johnson',
    'Williams',
    'Jones',
    'Brown',
    'Davis',
    'Miller',
    'Wilson',
    'Moore',
    'Taylor'
  ];

  //Creates a random contact
  static Contact createRandomContact() {
    String firstname = (_firstNames..shuffle()).first;
    String lastname = (_lastNames..shuffle()).first;
    String username = firstname.characters.first + lastname;
    return Contact(
      id: Firestorm.randomID(),
      firstname: firstname,
      lastname: lastname,
      email: '$username@example.com',
      phone: '+1-555-${(1000 + (9999 * (DateTime.now().millisecondsSinceEpoch % 10000))).toString().substring(1)}',
      address: Address.createRandomAddress(),
      createdAt: DateTime.now().millisecondsSinceEpoch,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is Contact && runtimeType == other.runtimeType &&
              id == other.id;

  @override
  int get hashCode => id.hashCode;

}