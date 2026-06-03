import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:flutter_test_app/custom_color.dart';
import 'package:flutter_test_app/person.dart';

@FirestormObject()
class WeirdData extends Person {
  String data;
  String? otherData;
  static int staticData = 42;
  int _privateData = 3;
  Map<String, int> aMap = {};
  List<String> aList = [];
  CustomColor color;

  int get privateData => _privateData;


  set privateData(int value) {
    _privateData = value;
  }

  WeirdData(this.data, this.color, {this.otherData = 'default'}) : super('id', 'first', 'last', 0, 0.0, false, []);
}