class Vehicle {

  String brand;
  String model;

  Vehicle(this.brand, this.model);

  void startEngine() {
    print('$brand $model\'s engine started.');
  }

  void stopEngine() {
    print('$brand $model\'s engine stopped.');
  }

}