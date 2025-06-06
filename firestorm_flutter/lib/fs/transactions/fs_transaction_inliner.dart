import 'fs_transaction_handler.dart';

/// A class that inlines Firestorm transactions.
class FSTransactionInliner extends FSTransactionHandler {
  final Future<void> Function(FSTransactionHandler tx) _logic;

  FSTransactionInliner(this._logic);

  @override
  Future<void> handleTransaction() => _logic(this);

}