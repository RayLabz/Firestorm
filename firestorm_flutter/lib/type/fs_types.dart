import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/element/type.dart';
import 'package:firestorm/gen/class_checker.dart';

class FSTypes {

  ///Checks if the given Dart type is supported by Firestore.
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
    else if (type.element != null && type is InterfaceType) {
      final uri = type.element3.library2.uri;
      final isSDKType = (uri.scheme == 'dart' || uri.scheme == 'package' && uri.pathSegments.first == 'flutter');
      if (isSDKType) {
        return false;
      };

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
            ClassElement fieldClassElement = field.type.element as ClassElement;
            bool b = isTypeSupported(field.type) && ClassChecker.isClassAnnotatedWithFirestormObject(fieldClassElement);
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

    //Firestore natives:
    return isFirestoreNativeType(type);
  }

  /// Checks if the given Dart type is a native Firestore type (e.g., Timestamp, DocumentReference, ...).
  static bool isFirestoreNativeType(DartType type) {
    if (type is InterfaceType) {
      final element = type.element;
      return fsNativeTypes.contains(element.name);
    }
    return false;
  }

  /// List of native Firestore types that are directly supported without conversion.
  static const List<String> fsNativeTypes = [
    'Timestamp',
    'DocumentReference',
    'GeoPoint',
    'Uint8List',
  ];

}