import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:firestorm/firestorm.dart';

@FirestormObject()
class Address {
  String id;
  String street;
  String city;

  Address(this.id, this.street, this.city);

  static List<String> _streets = [
    'Main St',
    'Second St',
    'Third St',
    'Fourth St',
    'Fifth St',
    'Sixth St',
    'Seventh St',
    'Eighth St',
    'Ninth St',
    'Tenth St'
  ];

  static List<String> _cities = [
    'Springfield',
    'Shelbyville',
    'Ogdenville',
    'North Haverbrook',
    'Capital City',
    'Cypress Creek',
    'Arlen',
    'Beaverton',
    'Hill Valley',
    'Twin Peaks'
  ];

  static createRandomAddress() {
    return Address(
      Firestorm.randomID(),
      (_streets..shuffle()).first,
      (_cities..shuffle()).first,
    );
  }

}