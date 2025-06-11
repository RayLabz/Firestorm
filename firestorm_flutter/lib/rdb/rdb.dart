import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/rdb/delegates/rdb_delete_delegate.dart';

import '../firestorm.dart';
import 'delegates/rdb_create_delegate.dart';
import 'delegates/rdb_exist_delegate.dart';
import 'delegates/rdb_get_delegate.dart';

/// The main class for Firestorm, providing access to Firestore and various operations.
class RDB {

  static late FirebaseDatabase rdb;
  static final Map<Type, Serializer> serializers = {};
  static final Map<Type, Deserializer> deserializers = {};

  //Operation delegates:
  static final RDBCreateDelegate create = RDBCreateDelegate();
  static final RDBGetDelegate get = RDBGetDelegate();
  static final RDBDeleteDelegate delete = RDBDeleteDelegate();
  static final RDBExistDelegate exists = RDBExistDelegate();

  /// Registers a serializer for a specific type. Needed for dynamically serializing objects.
  /// NOTE: This function is called by generated code in the target Flutter app.
  /// You should not call this function directly in your code.
  static void registerSerializer<T>(Map<String, dynamic> Function(T obj) function) {
    serializers[T] = (dynamic obj) => function(obj as T);
  }

  /// Registers a deserializer for a specific type. Needed for dynamically deserializing objects.
  /// NOTE: This function is called by generated code in the target Flutter app.
  /// You should not call this function directly in your code.
  static void registerDeserializer<T>(T Function(Map<String, dynamic>) fromMap) {
    deserializers[T] = fromMap;
  }

  /// Initializes the Firestore instance. This should be called before any other Firestore operations.
  static init() async {
    await Firebase.initializeApp();
    rdb = FirebaseDatabase.instance;
  }

  /// Constructs a path for a RDB object based on its type and ID.
  static String constructPathForClassAndID(Type type, String id, {String? subcollection}) {
    if (subcollection == null) {
      return "${type.toString()}/$id";
    }
    return "${type.toString()}/$subcollection/$id";
  }

  /// Enables local caching for Firestore data.
  // static enableCaching() async {
  //   //WEB:
  //   if (kIsWeb) {
  //     await firestore.enablePersistence(const PersistenceSettings(synchronizeTabs: true));
  //   }
  //   //MOBILE (iOS & Android)
  //   else {
  //     firestore.settings = const Settings(persistenceEnabled: true);
  //   }
  // }

  /// Disables local caching for Firestore data.
  // static disableCaching() async {
  //   //WEB:
  //   if (kIsWeb) {
  //     print("!!! Disabling caching is not supported on web. Caching is always on once enabled. You must restart your app and avoid calling enableCaching().");
  //   }
  //   //MOBILE (iOS & Android)
  //   else {
  //     firestore.settings = const Settings(persistenceEnabled: false);
  //   }
  // }

  /// Enables network access for Firestore.
  // static Future<void> enableNetwork() async {
  //   await firestore.enableNetwork();
  // }

  /// Disables network access for Firestore.
  // static Future<void> disableNetwork() async {
  //   await firestore.disableNetwork();
  // }

}