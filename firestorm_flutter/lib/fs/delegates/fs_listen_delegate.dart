import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';

import '../fs.dart';

typedef ObjectListener<T> = void Function(T object, );

class FSListenDelegate {

  /// Registers a listener for changes to a specific object in Firestore.
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
    return handleListeners(docRef, deserializer, onDelete, onNull, onCreate, onChange);
  }

  /// Registers a listener for changes to a specific object in Firestore.
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
    return handleListeners(docRef, deserializer, onDelete, onNull, onCreate, onChange);
  }

  StreamSubscription<T?> handleListeners<T>(
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



  //TODO - Register class listener

  //TODO - Unregister class listener
}