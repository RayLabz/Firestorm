import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm_flutter/reflector.dart';
import 'package:reflectable/mirrors.dart';
import 'document_converter.dart';
import 'firestorm_reflector.dart';

class Firestorm {

  static Future<void> create<T>(T object) {
    String? id = FirestormReflector.getIdFieldValue(object!);
    if (id == null) {
      throw Exception("ID field cannot be null.");
    }
    ClassMirror classMirror = reflector.reflect(object).type;
    return FirebaseFirestore.instance
        .collection(classMirror.simpleName)
    .doc(id)
    .set(DocumentConverter.objectToMap(object));
  }

  static Future<T> get<T>(Type type, String id) async {
    DocumentSnapshot<Map<String, dynamic>> documentSnapshot = await FirebaseFirestore.instance
        .collection(type.toString())
        .doc(id)
        .get();
    T object = DocumentConverter.mapToObject(type, documentSnapshot.data()!) as T;
    return object;
  }

}