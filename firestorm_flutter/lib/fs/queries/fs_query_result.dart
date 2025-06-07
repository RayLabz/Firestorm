import 'package:cloud_firestore/cloud_firestore.dart';

/// Represents the result of a Firestore query, containing a list of objects and the snapshot of the last document, if any.
class FSQueryResult<T> {

  final List<T> _items;
  final QueryDocumentSnapshot? _snapshot;
  final String? _lastDocumentId;

  FSQueryResult(this._items, this._snapshot, this._lastDocumentId);

  String? get lastDocumentId => _lastDocumentId;

  List<T> get items => _items;

  QueryDocumentSnapshot? get snapshot => _snapshot;

}