import 'dart:async';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firestorm/commons/delegate/listen_delegate.dart';

import '../../firestorm.dart';
import '../fs.dart';

/// A specialized type definition for a function that executes when an object is changed.
typedef ObjectListener<T> = void Function(T object);

/// A delegate class to listen to changes in Firestore documents.
class FSListenDelegate implements ListenDelegate {

  /// Listens to changes in an object.
  @override
  StreamSubscription<T?> toObject<T>(
    dynamic object, {
    void Function(T object)? onCreate,
    void Function(T object)? onChange,
    void Function()? onDelete,
    void Function()? onNull,
    String? subcollection,
  }) {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    var docRef = FS.instance.collection(object.runtimeType.toString()).doc(object.id);
    if (subcollection != null) {
      docRef = FS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(object.id);
    }
    return _handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange);
  }

  /// Listens to changes in an object using its ID and type.
  @override
  StreamSubscription<T?> toID<T>(
      Type type, String id, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
        String? subcollection,
      }) {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    var docRef = FS.instance.collection(type.toString()).doc(id);
    if (subcollection != null) {
      docRef = FS.instance.collection(type.toString()).doc(subcollection).collection(subcollection).doc(id);
    }
    return _handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange);
  }

  /// Listens to changes in multiple objects.
  @override
  List<StreamSubscription<T?>> toObjects<T>(
      List<dynamic> objects, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
        String? subcollection,
      }) {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    List<StreamSubscription<T?>> subscriptions = [];
    for (final object in objects) {
      var docRef = FS.instance.collection(object.runtimeType.toString()).doc(object.id);
      if (subcollection != null) {
        docRef = FS.instance.collection(object.runtimeType.toString()).doc(subcollection).collection(subcollection).doc(object.id);
      }
      subscriptions.add(_handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange));
    }
    return subscriptions;
  }

  /// Listens to changes in multiple objects using their IDs and type.
  @override
  List<StreamSubscription<T?>> toIDs<T>(
      Type type, List<String> ids, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function()? onDelete,
        void Function()? onNull,
        String? subcollection,
      }) {
    Deserializer? deserializer = FS.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    List<StreamSubscription<T?>> subscriptions = [];
    for (final String id in ids) {
      var docRef = FS.instance.collection(type.toString()).doc(id);
      if (subcollection != null) {
        docRef = FS.instance.collection(type.toString()).doc(subcollection).collection(subcollection).doc(id);
      }
      subscriptions.add(_handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange));
    }
    return subscriptions;
  }

  /// Converts data from a stream of Firestore documents into a stream of objects.
  StreamSubscription<T?> _handleDocumentListener<T>(
      DocumentReference docRef,
      Deserializer deserializer,
      Function()? onDelete,
      Function()? onNull,
      Function(T object)? onCreate,
      Function(T object)? onChange,
      ) {
    T? previous; //keeps track if this object previously existed.

    //Map stream of snapshots to stream of deserialized objects.
    final Stream<T?> mappedStream = docRef.snapshots().map((snapshot) {
      if (!snapshot.exists) {
        return null; // Document does not exist.
      }
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