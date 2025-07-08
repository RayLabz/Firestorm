import 'dart:async';

/// Contains the operations for listen delegates.
abstract class ListenDelegate {

  /// Listens to changes in an object.
  StreamSubscription<T?> toObject<T>(
      dynamic object, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
        String? subcollection,
      });

  /// Listens to changes in an object using its ID and type.
  StreamSubscription<T?> toID<T>(
      Type type, String id, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
        String? subcollection,
      });

  /// Listens to changes in multiple objects.
  List<StreamSubscription<T?>> toObjects<T>(
      List<dynamic> objects, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
        String? subcollection,
      });

  /// Listens to changes in multiple objects using their IDs and type.
  List<StreamSubscription<T?>> toIDs<T>(
      Type type, List<String> ids, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
        String? subcollection,
      });

}