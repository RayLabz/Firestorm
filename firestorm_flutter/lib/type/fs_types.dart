import 'package:analyzer/dart/element/element.dart';
import 'package:analyzer/dart/element/type.dart';

class FSTypes {

  ///Checks if the given variable is a supported Firestore type.
  // static bool isVariableSupportedType(variable) {
  //   if (variable is String) {
  //     return true;
  //   } else if (variable is int) {
  //     return true;
  //   } else if (variable is double) {
  //     return true;
  //   } else if (variable is bool) {
  //     return true;
  //   } else if (variable is DateTime) {
  //     return true;
  //   } else if (variable is Map<String, dynamic>) {
  //     return true;
  //   } else if (variable is List) {
  //     return true;
  //   } else if (variable is DocumentReference) {
  //     return true;
  //   } else if (variable is GeoPoint) {
  //     return true;
  //   } else if (variable == null) {
  //     return true;
  //   } else if (variable is Uint8List) {
  //     return true;
  //   }
  //   return false;
  // }

  ///Checks if the given Dart type is supported by Firestore.
  static bool isTypeSupported(DartType type) {

    //primitives
    if (type.isDartCoreString ||
        type.isDartCoreInt ||
        type.isDartCoreDouble ||
        type.isDartCoreBool ||
        type.getDisplayString() == 'DateTime' ||
        type.isDartCoreNull ||
        type.getDisplayString() == 'DocumentReference' ||
        type.getDisplayString() == 'GeoPoint' ||
        type.getDisplayString() == 'Uint8List'
    ) {
      // print('Type ${type.getDisplayString()} is supported by Firestore. (PRIMITIVE)'); //todo remove
      return true;
    }

    //lists, must contain supported elements
    else if (type.isDartCoreList) {
      final listType = type as InterfaceType;
      if (listType.typeArguments.length == 1 &&
          isTypeSupported(listType.typeArguments[0])) {
        // print('Type ${type.getDisplayString()} is supported by Firestore. (LIST)'); //todo remove
        return true;
      }
    }

    //maps, must have String keys and supported values
    else if (type.isDartCoreMap) {
      final mapType = type as InterfaceType;
      if (mapType.typeArguments.length == 2 &&
          mapType.typeArguments[0].isDartCoreString &&
          isTypeSupported(mapType.typeArguments[1])) {
        // print('Type ${type.getDisplayString()} is supported by Firestore. (MAP)'); //todo remove
        return true;
      }
    }

    //check user-defined types:
    else if (type.element != null && !type.element!.library!.isDartCore && type is InterfaceType) {
      // print("Checking user-defined type: ${type.getDisplayString()}"); //todo remove
      final element = type.element;
      // print("Element: ${element.name} ${element.runtimeType}"); //todo remove
      if (element is ClassElement) {
        List<FieldElement> allFields = [];
        allFields.addAll(element.fields);
        for (final parent in element.allSupertypes) { //Check parent classes too
          if (parent.getDisplayString() != 'Object') { //Avoid the Object class
            allFields.addAll(parent.element.fields);
          }
        }

        // print("Found ${allFields.length} fields in class ${element.name} and its parents."); //todo remove

        List<bool> fieldsSupported = [];
        for (var field in allFields) {
          // print("Checking field: ${field.name} of type ${field.type.getDisplayString()}"); //todo remove
          if (!field.isStatic) {
            bool b = isTypeSupported(field.type);
            if (!b) {
              // print("Field ${field.name} of type ${field.type.getDisplayString()} is NOT supported by Firestore."); //todo remove
            }
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

}