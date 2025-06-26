import 'package:flutter_test_app/evaluation/vehicle.dart';

class Car extends Vehicle {

  int doors;

  Car(String brand, String model, this.doors) : super(brand, model);

  void openTrunk() {
    print('Opening trunk of $brand $model.');
  }

}