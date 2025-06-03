import 'dart:nativewrappers/_internal/vm/lib/typed_data_patch.dart' show Uint8List;

import 'package:cloud_firestore/cloud_firestore.dart';

class FSTypes {

  ///Checks if the given variable is a supported Firestore type.
  static bool isSupportedFSType(variable) {
    if (variable is String) {
      return true;
    } else if (variable is int) {
      return true;
    } else if (variable is double) {
      return true;
    } else if (variable is bool) {
      return true;
    } else if (variable is DateTime) {
      return true;
    } else if (variable is Map<String, dynamic>) {
      return true;
    } else if (variable is List) {
      return true;
    } else if (variable is DocumentReference) {
      return true;
    } else if (variable is GeoPoint) {
      return true;
    } else if (variable == null) {
      return true;
    } else if (variable is Uint8List) {
      return true;
    }
    return false;
  }

}