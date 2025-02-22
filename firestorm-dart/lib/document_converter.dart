import 'package:firestorm_flutter/reflector.dart';
import 'package:reflectable/reflectable.dart';

class DocumentConverter {

  static Map<String, dynamic> objectToMap(Object object) {
    InstanceMirror instanceMirror = reflector.reflect(object);
    ClassMirror classMirror = instanceMirror.type;
    Map<String, dynamic> data = {};
    for (DeclarationMirror declarationMirror in classMirror.declarations.values) {
      if (declarationMirror is VariableMirror) {
        if (_isBasicReflectedType(declarationMirror.reflectedType)) {
          data[declarationMirror.simpleName] = instanceMirror.invokeGetter(declarationMirror.simpleName);
        }
        else if (_checkVariableType(declarationMirror.type)) {
          data[declarationMirror.simpleName] = instanceMirror.invokeGetter(declarationMirror.simpleName);
        }
      }
    }
    return data;
  }

  static T mapToObject<T>(Type type, Map<String, dynamic> map) {
    TypeMirror typeMirror = reflector.reflectType(type);
    ClassMirror classMirror = typeMirror as ClassMirror;

    Object object = classMirror.newInstance("fs", [], {});
    InstanceMirror instanceMirror = reflector.reflect(object);
    for (DeclarationMirror declarationMirror in classMirror.declarations.values) {
      if (declarationMirror is VariableMirror) {
        //TODO - What happens if a field is required?
        if (map[declarationMirror.simpleName] != null) {
          instanceMirror.invokeSetter(declarationMirror.simpleName, map[declarationMirror.simpleName]);
        }
      }
    }
    return object as T;
  }

  ///Accepted types:
  ///int, double, bool, String, List, Map
  static bool _checkVariableType(TypeMirror typeMirror) {
    if (_isBasicType(typeMirror)) {
      return true;
    }
    if (typeMirror == reflector.reflectType(List)) {
      TypeMirror listTypeMirror = typeMirror.typeArguments[0];
      if (_isBasicType(listTypeMirror) || _isValidMap(listTypeMirror)) {
        return true;
      }
      return false;
    }
    if (typeMirror == reflector.reflectType(Map)) {
      return _isValidMap(typeMirror);
    }
    //TODO - ENUMS!
    //TODO - OBJECTS!
    return false;
  }

  static bool _isBasicType(TypeMirror typeMirror) {
    if (typeMirror == reflector.reflectType(int)) {
      return true;
    }
    if (typeMirror == reflector.reflectType(double)) {
      return true;
    }
    if (typeMirror == reflector.reflectType(bool)) {
      return true;
    }
    if (typeMirror == reflector.reflectType(String)) {
      return true;
    }
    return false;
  }

  static bool _isBasicReflectedType(Type type) {
    if (type == int) {
      return true;
    }
    if (type == double) {
      return true;
    }
    if (type == bool) {
      return true;
    }
    if (type == String) {
      return true;
    }
    return false;
  }

  static bool _isValidMap(TypeMirror typeMirror) {
    if (typeMirror.reflectedTypeArguments[0] != String) {
      return false;
    }
    return _checkVariableType(typeMirror.typeArguments[1]);
  }

}