class Vehicle {

  String id;
  String brand;
  String model;

  Vehicle(this.id, this.brand, this.model);

  void startEngine() {
    print('$brand $model\'s engine started.');
  }

  void stopEngine() {
    print('$brand $model\'s engine stopped.');
  }

}