import 'package:firestorm/annotations/firestorm_object.dart';
import 'package:flutter_test_app/evaluation/data/vehicle.dart';

@FirestormObject()
class Car extends Vehicle {

  int doors;

  Car(String id, String brand, String model, this.doors) : super(id, brand, model);

  void openTrunk() {
    print('Opening trunk of $brand $model.');
  }

  Map<String, dynamic> toMap() {
    return {
      "id": id,
      "brand": brand,
      "model": model,
      "doors": doors,
    };
  }

  factory Car.fromMap(Map<String, dynamic> map) {
    return Car(
      map["id"],
      map["brand"],
      map["model"],
      map["doors"],
    );
  }

}