import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/exceptions/null_id_exception.dart';

import '../firestorm.dart';


class FS {

  static late FirebaseFirestore firestore;
  static final Map<Type, Serializer> _serializers = {};

  /// Registers a serializer for a specific type. Needed for dynamically serializing objects.
  static void register<T>(Map<String, dynamic> Function(T obj) function) {
    _serializers[T] = (dynamic obj) => function(obj as T);
  }

  /// Initializes the Firestore instance. This should be called before any other Firestore operations.
  static init() async {
    await Firebase.initializeApp();
    firestore = FirebaseFirestore.instance;
  }

  /// Returns the Firestore instance. This should be used after calling `init()`.
  static FirebaseFirestore getInstance() {
    return firestore;
  }

  /// Creates a document in Firestore from the given object.
  static Future<void> create(dynamic object) async {
    final serializer = _serializers[object.runtimeType];
    if (serializer == null) {
      throw UnsupportedError('Serializer not registered for type: ${object.runtimeType}');
    }
    final map = serializer(object);
    String id = map["id"];
    if (id.isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = firestore.collection(object.runtimeType.toString()).doc(id);
    return ref.set(map);
  }

}