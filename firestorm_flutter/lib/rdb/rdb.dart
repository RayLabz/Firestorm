import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/rdb/delegates/rdb_delete_delegate.dart';
import 'package:firestorm/rdb/delegates/rdb_list_delegate.dart';
import 'package:firestorm/rdb/delegates/rdb_listen_delegate.dart';
import 'package:firestorm/rdb/delegates/rdb_reference_delegate.dart';

import '../firestorm.dart';
import 'delegates/rdb_create_delegate.dart';
import 'delegates/rdb_exist_delegate.dart';
import 'delegates/rdb_get_delegate.dart';
import 'delegates/rdb_update_delegate.dart';

/// The main class for Firestorm, providing access to Firestore and various operations.
class RDB {

  static late FirebaseDatabase instance;
  static final Map<Type, Serializer> serializers = {};
  static final Map<Type, Deserializer> deserializers = {};
  static final Map<Type, String> classNames = {};

  //Operation delegates:
  static final RDBCreateDelegate create = RDBCreateDelegate();
  static final RDBGetDelegate get = RDBGetDelegate();
  static final RDBDeleteDelegate delete = RDBDeleteDelegate();
  static final RDBExistDelegate exists = RDBExistDelegate();
  static final RDBListDelegate list = RDBListDelegate();
  static final RDBUpdateDelegate update = RDBUpdateDelegate();
  static final RDBReferenceDelegate reference = RDBReferenceDelegate();
  static final RDBListenDelegate listen = RDBListenDelegate();

  /// Registers a serializer for a specific type. Needed for dynamically serializing objects.
  /// NOTE: This function is called by generated code in the target Flutter app.
  /// You should not call this function directly in your code.
  static void registerSerializer<T>(Map<String, dynamic> Function(T obj) function) {
    serializers[T] = (dynamic obj) => function(obj as T);
    if (Firestorm.debug) Firestorm.log.i("Registered serializer for type: $T");
  }

  /// Registers a deserializer for a specific type. Needed for dynamically deserializing objects.
  /// NOTE: This function is called by generated code in the target Flutter app.
  /// You should not call this function directly in your code.
  static void registerDeserializer<T>(T Function(Map<String, dynamic>) fromMap) {
    deserializers[T] = fromMap;
    if (Firestorm.debug) Firestorm.log.i("Registered deserializer for type: $T");
  }

  /// Registers a type name for release config compatibility (avoiding code obfuscation issues):
  static void registerClassName(Type type, String typeName) {
    classNames[type] = typeName;
    if (Firestorm.debug)  Firestorm.log.i("Registered class name: $typeName for type: $type");
  }

  /// Initializes the RDB instance. This should be called before any other Firestore operations.
  static init() async {
    await Firebase.initializeApp();
    instance = FirebaseDatabase.instance;
    if (Firestorm.debug) Firestorm.log.i("Initialized Firestore");
  }

  /// Initializes the RDB instance with custom options.
  static initWithOptions(FirebaseOptions options) async {
    await Firebase.initializeApp(options: options);
    instance = FirebaseDatabase.instance;
    if (Firestorm.debug) Firestorm.log.i("Initialized Firestore with options");
  }

  /// Constructs a path for a RDB object based on its type and ID.
  static String constructPathForClassAndID(Type type, String id, {String? subcollection}) {
    if (subcollection == null) {
      return "${type.toString()}/$id";
    }
    return "${type.toString()}:$subcollection/$id";
  }

  /// Constructs a path for a class.
  static String constructPathForClass(Type type, {String? subcollection}) {
    if (subcollection == null) {
      return type.toString();
    }
    return "${type.toString()}:$subcollection";
  }

  /// Enables local caching for Firestore data.
  static enableCaching() async {
      instance.setPersistenceEnabled(true);
  }

  /// Disables local caching for Firestore data.
  static disableCaching() async {
    instance.setPersistenceEnabled(false);
  }

  /// Configures RDB to use the emulator instead of the real database.
  static useEmulator(final String host, final int port) {
    instance.useDatabaseEmulator(host, port);
  }

  /// Shuts down the RDB instance. If used again, a new instance has to be created by calling [RDB.init()]
  static Future<void> shutdown() {
    return instance.goOffline();
  }

}