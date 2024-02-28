import 'package:firestorm_flutter/reflector.dart';
import 'package:reflectable/mirrors.dart';

class FirestormReflector {

  static String? getIdFieldValue(Object object) {
    InstanceMirror instanceMirror = reflector.reflect(object);
    // ClassMirror classMirror = instanceMirror.type;
    var result = instanceMirror.invokeGetter("id");
    if (result is String) {
      return result;
    }
    return null;
  }

}