import 'package:firebase_database/firebase_database.dart';

/// Represents the result of a Firestore query, containing a list of objects and the snapshot of the last document, if any.
class RDBQueryResult<T> {

  final List<T> _items;
  final DataSnapshot? _snapshot;
  final String? _lastDocumentId;

  RDBQueryResult(this._items, this._snapshot, this._lastDocumentId);

  String? get lastDocumentId => _lastDocumentId;

  List<T> get items => _items;

  DataSnapshot? get snapshot => _snapshot;

}