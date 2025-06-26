import 'package:flutter_test_app/evaluation/vehicle.dart';

class Motorcycle extends Vehicle {

  String type;

  Motorcycle(String brand, String model, this.type) : super(brand, model);

  void popWheelie() {
    print('$brand $model is popping a wheelie!');
  }

}