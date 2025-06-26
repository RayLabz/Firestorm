import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:flutter_test_app/evaluation/vehicle.dart';

@FirestormObject()
class Motorcycle extends Vehicle {

  String type;

  Motorcycle(String id, String brand, String model, this.type) : super(id, brand, model);

  void popWheelie() {
    print('$brand $model is popping a wheelie!');
  }

  Map<String, dynamic> toMap() {
    return {
      "id": id,
      "brand": brand,
      "model": model,
      "type": type
    };
  }

  factory Motorcycle.fromMap(Map<String, dynamic> map) {
    return Motorcycle(
      map["id"],
      map["brand"],
      map["model"],
      map["type"]
    );
  }

}