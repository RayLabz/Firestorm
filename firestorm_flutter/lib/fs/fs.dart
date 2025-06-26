import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/fs/delegates/fs_get_delegate.dart';
import 'package:firestorm/fs/delegates/fs_listen_delegate.dart';
import 'package:firestorm/fs/delegates/fs_reference_delegate.dart';
import 'package:firestorm/fs/queries/fs_paginator.dart';
import 'package:flutter/foundation.dart';

import '../firestorm.dart';
import 'delegates/fs_batch_delegate.dart';
import 'delegates/fs_create_delegate.dart';
import 'delegates/fs_delete_delegate.dart';
import 'delegates/fs_exist_delegate.dart';
import 'delegates/fs_list_delegate.dart';
import 'delegates/fs_transaction_delegate.dart';
import 'delegates/fs_update_delegate.dart';

/// The main class for Firestorm, providing access to Firestore and various operations.
class FS {

  static late FirebaseFirestore instance;
  static final Map<Type, Serializer> serializers = {};
  static final Map<Type, Deserializer> deserializers = {};
  static bool multithreadingEnabled = false;

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

  /// Creates a paginator for Firestore queries.
  static FSPaginator<T> paginate<T>({ String? lastDocumentID, int limit = 10, String? subcollection }) {
    return FSPaginator<T>(lastDocumentID: lastDocumentID, numOfDocuments: limit, subcollection: subcollection);
  }

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
    instance = FirebaseFirestore.instance;
  }

  /// Initializes the Firestore instance with custom options.
  static initWithOptions(FirebaseOptions options) async {
    await Firebase.initializeApp(options: options);
    instance = FirebaseFirestore.instance;
  }

  /// Enables local caching for Firestore data.
  static enableCaching() async {
    //WEB:
    if (kIsWeb) {
      await instance.enablePersistence(const PersistenceSettings(synchronizeTabs: true));
    }
    //MOBILE (iOS & Android)
    else {
      instance.settings = const Settings(persistenceEnabled: true);
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
      instance.settings = const Settings(persistenceEnabled: false);
    }
  }

  /// Enables network access for Firestore.
  static Future<void> enableNetwork() async {
    await instance.enableNetwork();
  }

  /// Disables network access for Firestore.
  static Future<void> disableNetwork() async {
    await instance.disableNetwork();
  }

  /// Configures Firestore to use the emulator instead of the real database.
  static useEmulator(final String host, final int port) {
    instance.useFirestoreEmulator(host, port);
  }

  /// Enables multithreading for Firestore operations.
  static enableMultithreading() {
    if (kIsWeb) {
      throw UnsupportedError('Multithreading is not supported on web.');
    }
  }

  /// Disables multithreading for Firestore operations.
  static disableMultithreading() {
    multithreadingEnabled = false;
  }

  /// Shuts down the Firestore instance. If used again, a new instance has to be created by calling [FS.init()]
  static Future<void> shutdown() {
    return instance.terminate();
  }

}