import 'dart:async';
import 'package:firebase_database/firebase_database.dart';
import 'package:firestorm/commons/delegate/listen_delegate.dart';
import 'package:firestorm/rdb/helpers/rdb_deserialization_helper.dart';

import '../../firestorm.dart';
import '../rdb.dart';

/// A specialized type definition for a function that executes when an object is changed.
typedef ObjectListener<T> = void Function(T object);

/// A delegate class to listen to changes in Firestore documents.
class RDBListenDelegate implements ListenDelegate{

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
    Deserializer? deserializer = RDB.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    DatabaseReference docRef = RDB.instance.ref(RDB.constructPathForClassAndID(object.runtimeType, object.id, subcollection: subcollection));
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
    Deserializer? deserializer = RDB.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    DatabaseReference docRef = RDB.instance.ref(RDB.constructPathForClassAndID(type, id, subcollection: subcollection));
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
    Deserializer? deserializer = RDB.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }
    List<StreamSubscription<T?>> subscriptions = [];
    for (final object in objects) {
      DatabaseReference docRef = RDB.instance.ref(RDB.constructPathForClassAndID(object.runtimeType, object.id, subcollection: subcollection));
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
    Deserializer? deserializer = RDB.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError('No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    List<StreamSubscription<T?>> subscriptions = [];
    for (final String id in ids) {
      DatabaseReference docRef = RDB.instance.ref(RDB.constructPathForClassAndID(type, id, subcollection: subcollection));
      subscriptions.add(_handleDocumentListener(docRef, deserializer, onDelete, onNull, onCreate, onChange));
    }
    return subscriptions;
  }


  /// Listens to changes in a class/collection [Type].
  @override
  List<StreamSubscription<T>> toType<T>(
      Type type, {
        void Function(T object)? onCreate,
        void Function(T object)? onChange,
        void Function(T object)? onDelete,
        void Function(List<T> objects)? onListChange,
        String? subcollection,
      }) {
    Deserializer? deserializer = RDB.deserializers[T];
    if (deserializer == null) {
      throw UnsupportedError(
          'No deserializer found for type: $T. Consider re-generating Firestorm data classes.');
    }

    // Determine the collection reference path
    // Assuming RDB.constructPathForClassAndID can handle a null ID for collection paths
    // or using the type string directly as a folder.
    DatabaseReference collectionRef = RDB.instance.ref(type.toString());
    if (subcollection != null) {
      collectionRef = RDB.instance.ref("${type.toString()}:$subcollection");
    }

    return [
      _handleCollectionListener<T>(
        collectionRef,
        deserializer,
        onCreate,
        onChange,
        onDelete,
        onListChange,
      )
    ];
  }

  /// Converts from a stream of Realtime database items into a stream of objects.
  StreamSubscription<T?> _handleDocumentListener<T>(
      DatabaseReference dbRef,
      Deserializer deserializer,
      Function()? onDelete,
      Function()? onNull,
      Function(T object)? onCreate,
      Function(T object)? onChange,
      ) {
    T? previous; //keeps track if this object previously existed.

    //Map stream of snapshots to stream of deserialized objects.
    final Stream<T?> mappedStream = dbRef.onValue.map((event) {
      final data = RDBDeserializationHelper.snapshotToMap(event.snapshot);
      return deserializer(data);
    },);

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
    },);
  }

  /// Internal helper to handle collection-level events for Realtime Database.
  StreamSubscription<T> _handleCollectionListener<T>(
      DatabaseReference dbRef,
      Deserializer deserializer,
      void Function(T object)? onCreate,
      void Function(T object)? onChange,
      void Function(T object)? onDelete,
      void Function(List<T> objects)? onListChange,
      ) {
    // Keep track of the full list for onListChange
    final Map<String, T> currentCache = {};

    // We use a StreamController to merge child events into a single Stream<T>
    // to match the return type StreamSubscription<T>
    final controller = StreamController<T>.broadcast();

    // 1. Handle Child Added (onCreate)
    dbRef.onChildAdded.listen((event) {
      final data = RDBDeserializationHelper.snapshotToMap(event.snapshot);
      final T object = deserializer(data) as T;

      currentCache[event.snapshot.key!] = object;
      onCreate?.call(object);
      onListChange?.call(currentCache.values.toList());
      controller.add(object);
    });

    // 2. Handle Child Changed (onChange)
    dbRef.onChildChanged.listen((event) {
      final data = RDBDeserializationHelper.snapshotToMap(event.snapshot);
      final T object = deserializer(data) as T;

      currentCache[event.snapshot.key!] = object;
      onChange?.call(object);
      onListChange?.call(currentCache.values.toList());
      controller.add(object);
    });

    // 3. Handle Child Removed (onDelete)
    dbRef.onChildRemoved.listen((event) {
      final data = RDBDeserializationHelper.snapshotToMap(event.snapshot);
      final T object = deserializer(data) as T;

      currentCache.remove(event.snapshot.key);
      onDelete?.call(object);
      onListChange?.call(currentCache.values.toList());
      controller.add(object);
    });

    return controller.stream.listen((_) {});
  }

}