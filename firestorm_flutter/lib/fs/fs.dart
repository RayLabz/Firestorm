import 'package:analyzer/dart/element/type.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:firestorm/exceptions/null_id_exception.dart';
import 'package:firestorm/fs/delegates/fs_get_delegate.dart';
import 'package:firestorm/fs/delegates/fs_listen_delegate.dart';
import 'package:firestorm/fs/delegates/fs_reference_delegate.dart';

import '../firestorm.dart';
import 'delegates/fs_batch_delegate.dart';
import 'delegates/fs_create_delegate.dart';
import 'delegates/fs_delete_delegate.dart';
import 'delegates/fs_exist_delegate.dart';
import 'delegates/fs_list_delegate.dart';
import 'delegates/fs_transaction_delegate.dart';
import 'delegates/fs_update_delegate.dart';

class FS {

  static late FirebaseFirestore firestore;
  static final Map<Type, Serializer> serializers = {};
  static final Map<Type, dynamic Function(Map<String, dynamic>)> deserializers = {};

  /// Registers a serializer for a specific type. Needed for dynamically serializing objects.
  static void registerSerializer<T>(Map<String, dynamic> Function(T obj) function) {
    serializers[T] = (dynamic obj) => function(obj as T);
  }

  /// Registers a deserializer for a specific type. Needed for dynamically deserializing objects.
  static void registerDeserializer<T>(T Function(Map<String, dynamic>) fromMap) {
    deserializers[T] = fromMap;
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

  //TODO-LATER - Get collections list (firestore.listCollections)

  //TODO-LATER - Enable caching







}