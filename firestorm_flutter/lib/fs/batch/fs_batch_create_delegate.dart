import 'package:cloud_firestore/cloud_firestore.dart';

import '../../exceptions/null_id_exception.dart';
import '../fs.dart';

/// A delegate class to create documents in Firestore, using batches.
class FSBatchCreateDelegate {

  final SetOptions? setOptions;
  final WriteBatch _batch;

  FSBatchCreateDelegate.init(this._batch, { this.setOptions });

  /// Creates a document in Firestore from the given object.
  Future<void> one(dynamic object, { String? subcollection }) async {
    final serializer = FS.serializers[object.runtimeType];
    final String? className = FS.classNames[object.runtimeType];
    if (serializer == null || className == null) {
      throw UnsupportedError('No serializer/class name found for type: ${object.runtimeType}. Consider re-generating Firestorm data classes.');
    }
    final map = serializer(object);
    String id = map["id"];
    if (id.isEmpty) {
      throw NullIDException(map);
    }
    DocumentReference ref = FS.instance.collection(className).doc(id);
    if (subcollection != null) {
      ref = FS.instance.collection(className).doc(subcollection).collection(subcollection).doc(id);
    }
    _batch.set(ref, map, setOptions);
  }

}