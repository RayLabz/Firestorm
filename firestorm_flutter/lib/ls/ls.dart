import 'package:localstore/localstore.dart';

import '../firestorm.dart';
import 'delegates/ls_create_delegate.dart';
import 'delegates/ls_delete_delegate.dart';
import 'delegates/ls_exist_delegate.dart';
import 'delegates/ls_get_delegate.dart';
import 'delegates/ls_list_delegate.dart';
import 'delegates/ls_reference_delegate.dart';
import 'delegates/ls_update_delegate.dart';

class LS {
  
  static Localstore instance = Localstore.instance;
  static final Map<Type, Serializer> serializers = {};
  static final Map<Type, Deserializer> deserializers = {};
  static final Map<Type, String> classNames = {};

  //Operation delegates:
  static final LSCreateDelegate create = LSCreateDelegate();
  static final LSGetDelegate get = LSGetDelegate();
  static final LSUpdateDelegate update = LSUpdateDelegate();
  static final LSDeleteDelegate delete = LSDeleteDelegate();
  static final LSListDelegate list = LSListDelegate();
  static final LSExistDelegate exists = LSExistDelegate();
  static final LSReferenceDelegate reference = LSReferenceDelegate();

  /// Creates a paginator for Firestore queries.
  /// Maybe in next versions.
  // static FSPaginator<T> paginate<T>({ String? lastDocumentID, int limit = 10, String? subcollection }) {
  //   return FSPaginator<T>(lastDocumentID: lastDocumentID, numOfDocuments: limit, subcollection: subcollection);
  // }

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

}