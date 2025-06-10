import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:flutter/cupertino.dart';

@FirestormObject()
class Crazy {
  String id;
  int ha;
  DateTime dateTime;
  Text text;

  Crazy(this.id, this.ha, this.dateTime, this.text);

}