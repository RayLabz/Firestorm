import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';

import '../fs.dart';

typedef ObjectListener<T> = void Function(T object, );

class FSListenDelegate {

  /// Listens to changes in an object.
  StreamSubscription<T?> toObject<T>(
    dynamic object, {
    void Function(T object)? onCreate,
    void Function(T object)? onChange,
    void Function()? onDelete,
    void Function()? onNull,
  }) {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    final docRef = FS.firestore.collection(object.runtimeType.toString()).doc(object.id);
    return _handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange);
  }

  /// Listens to changes in an object using its ID and type.
  StreamSubscription<T?> toID<T>(
      Type type, String id, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
      }) {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    final docRef = FS.firestore.collection(type.toString()).doc(id);
    return _handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange);
  }

  /// Listens to changes in multiple objects.
  List<StreamSubscription<T?>> toObjects<T>(
      List<dynamic> objects, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
      }) {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    List<StreamSubscription<T?>> subscriptions = [];
    for (final object in objects) {
      final docRef = FS.firestore.collection(object.runtimeType.toString()).doc(object.id);
      subscriptions.add(_handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange));
    }
    return subscriptions;
  }

  /// Listens to changes in multiple objects using their IDs and type.
  List<StreamSubscription<T?>> toIDs<T>(
      Type type, List<String> ids, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
      }) {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    List<StreamSubscription<T?>> subscriptions = [];
    for (final String id in ids) {
      final docRef = FS.firestore.collection(type.toString()).doc(id);
      subscriptions.add(_handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange));
    }
    return subscriptions;
  }

  StreamSubscription<T?> _handleDocumentListener<T>(
      DocumentReference docRef,
      Deserializer deserializer,
      Function()? onDelete,
      Function()? onNull,
      Function(T object)? onCreate,
      Function(T object)? onChange
      ) {
    T? previous; //keeps track if this object previously existed.

    //Map stream of snapshots to stream of deserialized objects.
    final Stream<T?> mappedStream = docRef.snapshots().map((snapshot) {
      final data = snapshot.data() as Map<String, dynamic>;
      return deserializer(data);
    });

    //Listen to the mapped stream and call the appropriate callbacks.
    return mappedStream.listen((current) {
      if (current == null) {
        if (previous != null) {
          previous = null;
          onDelete?.call();
        }
        else {
          onNull?.call();
        }
        return;
      }

      if (previous == null) {
        onCreate?.call(current);
      } else {
        onChange?.call(current);
      }

      previous = current;
    });
  }

}