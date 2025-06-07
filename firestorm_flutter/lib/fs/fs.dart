import 'package:analyzer/dart/element/type.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/exceptions/null_id_exception.dart';
import 'package:firestorm/fs/delegates/fs_get_delegate.dart';
import 'package:firestorm/fs/delegates/fs_listen_delegate.dart';
import 'package:firestorm/fs/delegates/fs_reference_delegate.dart';
import 'package:flutter/foundation.dart';

import '../firestorm.dart';
import 'delegates/fs_batch_delegate.dart';
import 'delegates/fs_create_delegate.dart';
import 'delegates/fs_delete_delegate.dart';
import 'delegates/fs_exist_delegate.dart';
import 'delegates/fs_list_delegate.dart';
import 'delegates/fs_transaction_delegate.dart';
import 'delegates/fs_update_delegate.dart';

typedef Serializer = Map<String, dynamic> Function(dynamic); //Used to dynamically serialize objects
typedef Deserializer = dynamic Function(Map<String, dynamic> map); //Used to dynamically deserialize objects

/// The main class for Firestorm, providing access to Firestore and various operations.
class FS {

  static late FirebaseFirestore firestore;
  static final Map<Type, Serializer> serializers = {};
  static final Map<Type, Deserializer> deserializers = {};

  //Operation delegates:
  static final FSCreateDelegate create = FSCreateDelegate();
  static final FSGetDelegate get = FSGetDelegate();
  static final FSUpdateDelegate update = FSUpdateDelegate();
  static final FSDeleteDelegate delete = FSDeleteDelegate();
  static final FSListDelegate list = FSListDelegate();
  static final FSExistDelegate exists = FSExistDelegate();
  static final FSReferenceDelegate reference = FSReferenceDelegate();
  static final FSListenDelegate listen = FSListenDelegate();
  static final FSTransactionDelegate transaction = FSTransactionDelegate();
  static final FSBatchDelegate batch = FSBatchDelegate();

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
    firestore = FirebaseFirestore.instance;
  }

  /// Enables local caching for Firestore data.
  static enableCaching() async {
    //WEB:
    if (kIsWeb) {
      await firestore.enablePersistence(const PersistenceSettings(synchronizeTabs: true));
    }
    //MOBILE (iOS & Android)
    else {
      firestore.settings = const Settings(persistenceEnabled: true);
    }
  }

  /// Disables local caching for Firestore data.
  static disableCaching() async {
    //WEB:
    if (kIsWeb) {
      print("!!! Disabling caching is not supported on web. Caching is always on once enabled. You must restart your app and avoid calling enableCaching().");
    }
    //MOBILE (iOS & Android)
    else {
      firestore.settings = const Settings(persistenceEnabled: false);
    }
  }

  //TODO-Subcollections?







}