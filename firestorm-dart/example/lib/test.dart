import 'package:reflectable/reflectable.dart';

class Reflector extends Reflectable {
  const Reflector() : super(invokingCapability, typingCapability, reflectedTypeCapability, declarationsCapability, typeCapability);
}

const reflector = Reflector();



