import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/element/type.dart';
import 'package:firestorm/gen/class_checker.dart';

class LSTypes {

  ///Checks if the given Dart type is supported by Localstore.
  static bool isTypeSupported(DartType type) {

    if (isSupportedPrimitive(type)) {
      return true;
    }

    //enums:
    else if (ClassChecker.isEnumType(type)) {
      return true;
    }

    //lists, must contain supported elements
    else if (type.isDartCoreList) {
      final listType = type as InterfaceType;
      if (listType.typeArguments.length == 1 &&
          isTypeSupported(listType.typeArguments[0])) {
        return true;
      }
    }

    //maps, must have String keys and supported values
    else if (type.isDartCoreMap) {
      final mapType = type as InterfaceType;
      if (mapType.typeArguments.length == 2 &&
          mapType.typeArguments[0].isDartCoreString &&
          isTypeSupported(mapType.typeArguments[1])) {
        return true;
      }
    }

    //check user-defined types:
    else if (type.element != null && !type.element!.library!.isDartCore && type is InterfaceType) {
      final element = type.element;
      if (element is ClassElement) {
        List<FieldElement> allFields = [];
        allFields.addAll(element.fields);
        for (final parent in element.allSupertypes) { //Check parent classes too
          if (parent.getDisplayString() != 'Object') { //Avoid the Object class
            allFields.addAll(parent.element.fields);
          }
        }

        List<bool> fieldsSupported = [];
        for (var field in allFields) {
          if (!field.isStatic) {
            bool b = isTypeSupported(field.type);
            fieldsSupported.add(b);
          }
        }

        //Return true only if all fields are supported
        for (final bool b in fieldsSupported) {
          if (!b) {
            return false;
          }
        }

        return true;
      }
    }

    return false;
  }

  ///Checks if the given Dart type is a supported primitive type for Firestore.
  static bool isSupportedPrimitive(DartType type) {
    // Core primitives (nullability-safe)
    if (type.isDartCoreString ||
        type.isDartCoreInt ||
        type.isDartCoreDouble ||
        type.isDartCoreBool ||
        type.isDartCoreNull) {
      return true;
    }

    // Interface types (DateTime, etc.)
    if (type is InterfaceType) {
      final element = type.element;

      switch (element.name) {
        case 'DateTime':
        case 'DocumentReference':
        case 'GeoPoint':
        case 'Uint8List':
          return true;
      }
    }

    return false;
  }

}